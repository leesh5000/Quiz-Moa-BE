package com.leesh.quiz.api.userprofile.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leesh.quiz.api.quiz.dto.quiz.QuizDto;
import com.leesh.quiz.api.userprofile.dto.answer.EditMyAnswerDto;
import com.leesh.quiz.api.userprofile.dto.answer.MyAnswerDto;
import com.leesh.quiz.api.userprofile.dto.quiz.EditMyQuizDto;
import com.leesh.quiz.api.userprofile.dto.user.UserProfileDto;
import com.leesh.quiz.api.userprofile.service.UserProfileService;
import com.leesh.quiz.domain.user.constant.Role;
import com.leesh.quiz.global.constant.PagingResponseDto;
import com.leesh.quiz.global.constant.UserInfo;
import com.leesh.quiz.global.jwt.dto.AccessToken;
import com.leesh.quiz.global.jwt.service.TokenService;
import com.leesh.quiz.testconfiguration.MvcTestConfiguration;
import com.leesh.quiz.testconfiguration.RestDocsConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("유저 프로필 API 테스트")
@WebMvcTest(UserProfileController.class)
@Import(MvcTestConfiguration.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs
class UserProfileControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserProfileService userProfileService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("유저 프로필 조회 테스트 - 정상 호출")
    @Test
    void getUserProfile_success() throws Exception {

        // given
        AccessToken accessToken = AccessToken.of(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0IjoxNjc1MjEwODc5LCJleHAiOjE2NzY0MjA0NzksInVzZXJJZCI6MX0.Fae1uwS2RPmSad_Uf7pWA8lNqW-MZtm6wP-MDIHwnp8dQpKgaDms3URZBnAG53V8uU-J1Tl0wPFVR6j5wIQS_Q",
                900);

        long userId = 1L;
        String email = "test1@gmail.com";
        given(tokenService.extractUserInfo(any(String.class)))
                .willReturn(UserInfo.of(userId, Role.USER));

        UserProfileDto.Answers answers = new UserProfileDto.Answers(13, 22);
        UserProfileDto.Quizzes quizzes = new UserProfileDto.Quizzes(3, 5);

        UserProfileDto userProfileDto = UserProfileDto.builder()
                .id(3L)
                .username("테스트 유저")
                .email("test1@gmail.com")
                .profileImageUrl("https://cdn.test.com")
                .quizzes(quizzes)
                .answers(answers)
                .build();

        given(userProfileService.getUserProfile(anyString()))
                .willReturn(userProfileDto);

        // when
        ResultActions result = mvc.perform(
                get("/api/users/{email}", email)
                        .header(HttpHeaders.AUTHORIZATION, accessToken.grantType() + " " + accessToken.accessToken())
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.username").value("테스트 유저"))
                .andExpect(jsonPath("$.email").value("test1@gmail.com"))
                .andExpect(jsonPath("$.profileImageUrl").value("https://cdn.test.com"))
                .andExpect(jsonPath("$.quizzes.totalCount").value(3))
                .andExpect(jsonPath("$.quizzes.totalVotesSum").value(5))
                .andExpect(jsonPath("$.answers.totalCount").value(13))
                .andExpect(jsonPath("$.answers.totalVotesSum").value(22));

        then(tokenService).should(times(1)).extractUserInfo(any(String.class));
        then(userProfileService).should(times(1)).getUserProfile(anyString());

        // API 문서화
        result
                .andDo(document("user-profile/get-user-profile",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("접근 토큰 (Access Token)")
                        ),
                        pathParameters(
                                parameterWithName("email").description("프로필 조회하려는 유저 이메일")
                        ),
                        responseFields(
                                fieldWithPath("id").description("유저 ID (PK값)"),
                                fieldWithPath("username").description("유저 이름"),
                                fieldWithPath("email").description("유저 이메일"),
                                fieldWithPath("profileImageUrl").description("유저 프로필 이미지 URL"),
                                fieldWithPath("quizzes.totalCount").description("유저가 작성한 퀴즈의 총 개수"),
                                fieldWithPath("quizzes.totalVotesSum").description("유저가 작성한 퀴즈가 받은 총 추천 수"),
                                fieldWithPath("answers.totalCount").description("유저가 작성한 답변의 총 개수"),
                                fieldWithPath("answers.totalVotesSum").description("유저가 작성한 답변이 받은 총 추천 수")
                        )));

    }

    @DisplayName("유저 퀴즈 목록 조회 API 테스트 - 정상 호출")
    @Test
    void getUserQuizzes_success() throws Exception {

        // given
        AccessToken accessToken = AccessToken.of(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0IjoxNjc1MjEwODc5LCJleHAiOjE2NzY0MjA0NzksInVzZXJJZCI6MX0.Fae1uwS2RPmSad_Uf7pWA8lNqW-MZtm6wP-MDIHwnp8dQpKgaDms3URZBnAG53V8uU-J1Tl0wPFVR6j5wIQS_Q",
                900);

        long userId = 1L;
        given(tokenService.extractUserInfo(any(String.class)))
                .willReturn(UserInfo.of(userId, Role.USER));

        QuizDto.Author author = QuizDto.Author.builder()
                .id(1L)
                .email("test1@gmail.com")
                .username("test1")
                .build();
        List<QuizDto> content = List.of(
                QuizDto.builder()
                        .id(1L)
                        .title("HTTP 프로토콜의 특징은 무엇인가요?")
                        .answerCount(5)
                        .totalVotes(12)
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .author(
                                author)
                        .build(),
                QuizDto.builder()
                        .id(2L)
                        .title("테스트 5 유저가 작성한 퀴즈")
                        .answerCount(0)
                        .totalVotes(3)
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .author(author)
                        .build()
        );

        int totalElements = 1125;
        int totalPages = 12;
        boolean last = false;
        boolean first = true;
        boolean empty = false;

        given(userProfileService.getUserQuizzesByPaging(any(Pageable.class), anyLong()))
                .willReturn(new PagingResponseDto<>(content, totalElements, totalPages, last, first, empty));

        // when
        ResultActions result = mvc.perform(
                get("/api/users/{userId}/quizzes?page={page}&size={size}&sort={property,direction}", userId, 0, 3, "id,asc")
                        .header(HttpHeaders.AUTHORIZATION, accessToken.grantType() + " " + accessToken.accessToken())
                        .accept(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].title").value("HTTP 프로토콜의 특징은 무엇인가요?"))
                .andExpect(jsonPath("$.content[0].answerCount").value(5))
                .andExpect(jsonPath("$.content[0].author").isNotEmpty())
                .andExpect(jsonPath("$.content[0].author.id").value(1L))
                .andExpect(jsonPath("$.content[0].author.email").value("test1@gmail.com"))
                .andExpect(jsonPath("$.content[0].author.username").value("test1"))
                .andExpect(jsonPath("$.content[0].totalVotes").value(12))
                .andExpect(jsonPath("$.content[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.content[0].modifiedAt").isNotEmpty())
                .andExpect(jsonPath("$.totalElements").value(totalElements))
                .andExpect(jsonPath("$.totalPages").value(totalPages))
                .andExpect(jsonPath("$.last").value(last))
                .andExpect(jsonPath("$.first").value(first))
                .andExpect(jsonPath("$.empty").value(empty));

        then(tokenService).should().extractUserInfo(any(String.class));
        then(userProfileService).should().getUserQuizzesByPaging(any(Pageable.class), anyLong());

        // API 문서화
        result
                .andDo(document("user-profile/get-user-quizzes",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("접근 토큰 (Access Token)")
                        ),
                        pathParameters(
                                parameterWithName("userId").description("유저 ID (PK값)")
                        ),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호 (0부터 시작)"),
                                parameterWithName("size").description("페이지 당 사이즈"),
                                parameterWithName("sort").description("{정렬 기준, 정렬 방향}")
                        ),
                        responseFields(
                                fieldWithPath("content").description("퀴즈 목록"),
                                fieldWithPath("content[].id").description("퀴즈 ID (PK값)"),
                                fieldWithPath("content[].title").description("퀴즈 제목"),
                                fieldWithPath("content[].answerCount").description("퀴즈 답변 수"),
                                fieldWithPath("content[].author").description("퀴즈 작성자"),
                                fieldWithPath("content[].author.id").description("퀴즈 작성자 ID (PK값)"),
                                fieldWithPath("content[].author.email").description("퀴즈 작성자 이메일"),
                                fieldWithPath("content[].author.username").description("퀴즈 작성자 이름"),
                                fieldWithPath("content[].totalVotes").description("이 퀴즈가 얻은 추천 수 (음수도 가능)"),
                                fieldWithPath("content[].createdAt").description("퀴즈 작성 시간"),
                                fieldWithPath("content[].modifiedAt").description("마지막 퀴즈 수정 시간"),
                                fieldWithPath("totalElements").description("전체 퀴즈 수"),
                                fieldWithPath("totalPages").description("전체 페이지 수"),
                                fieldWithPath("last").description("마지막 페이지 여부"),
                                fieldWithPath("first").description("첫 페이지 여부"),
                                fieldWithPath("empty").description("빈 페이지 여부"))));

    }

    @DisplayName("유저 퀴즈 수정 API 테스트 - 정상 호출")
    @Test
    void editMyQuiz_success() throws Exception {

        // given
        AccessToken accessToken = AccessToken.of(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0IjoxNjc1MjEwODc5LCJleHAiOjE2NzY0MjA0NzksInVzZXJJZCI6MX0.Fae1uwS2RPmSad_Uf7pWA8lNqW-MZtm6wP-MDIHwnp8dQpKgaDms3URZBnAG53V8uU-J1Tl0wPFVR6j5wIQS_Q",
                900);

        long quizWriterId = 1L;
        long editQuizId = 1L;
        given(tokenService.extractUserInfo(any(String.class)))
                .willReturn(UserInfo.of(quizWriterId, Role.USER));

        given(userProfileService.editMyQuiz(any(EditMyQuizDto.Request.class), any(UserInfo.class), anyLong()))
                .willReturn(EditMyQuizDto.Response.from(editQuizId));

        EditMyQuizDto.Request requestBody = EditMyQuizDto.Request.builder()
                .title("수정된 퀴즈 제목, 퀴즈 제목은 10자 이상이어야 합니다.")
                .contents("수정된 퀴즈 내용")
                .build();

        // when
        ResultActions result = mvc.perform(
                put("/api/users/{userId}/quizzes/{quizId}", quizWriterId, editQuizId)
                        .header(HttpHeaders.AUTHORIZATION, accessToken.grantType() + " " + accessToken.accessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.editQuizId").value(editQuizId));

        then(tokenService).should(times(1)).extractUserInfo(any(String.class));
        then(userProfileService).should(times(1)).editMyQuiz(any(EditMyQuizDto.Request.class), any(UserInfo.class), anyLong());

        // API 문서화
        result
                .andDo(document("user-profile/edit-my-quiz",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("접근 토큰(Access Token)")
                        ),
                        pathParameters(
                                parameterWithName("userId").description("퀴즈 작성자 ID"),
                                parameterWithName("quizId").description("수정할 퀴즈 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").description("퀴즈 제목").attributes(RestDocsConfiguration.field("constraints", "10자 이상 255자 이하")),
                                fieldWithPath("contents").description("퀴즈 내용").attributes(RestDocsConfiguration.field("constraints", "10자 이상"))
                        ),
                        responseFields(
                                fieldWithPath("editQuizId").description("수정된 퀴즈 ID"))));

    }

    @DisplayName("유저 퀴즈 삭제 API 테스트 - 정상 호출")
    @Test
    void deleteMyQuiz_success() throws Exception {

        // given
        AccessToken accessToken = AccessToken.of(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0IjoxNjc1MjEwODc5LCJleHAiOjE2NzY0MjA0NzksInVzZXJJZCI6MX0.Fae1uwS2RPmSad_Uf7pWA8lNqW-MZtm6wP-MDIHwnp8dQpKgaDms3URZBnAG53V8uU-J1Tl0wPFVR6j5wIQS_Q",
                900);

        long quizWriterId = 1L;
        long deleteQuizId = 1L;
        given(tokenService.extractUserInfo(any(String.class)))
                .willReturn(UserInfo.of(quizWriterId, Role.USER));

        willDoNothing().given(userProfileService).deleteMyQuiz(anyLong(), any(UserInfo.class));

        // when
        ResultActions result = mvc.perform(
                delete("/api/users/{userId}/quizzes/{quizId}", quizWriterId, deleteQuizId)
                        .header(HttpHeaders.AUTHORIZATION, accessToken.grantType() + " " + accessToken.accessToken())
                        .accept(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(status().isNoContent());

        then(userProfileService).should(times(1)).deleteMyQuiz(anyLong(), any(UserInfo.class));
        then(tokenService).should(times(1)).extractUserInfo(any(String.class));

        // API 문서화
        result
                .andDo(document("user-profile/delete-my-quiz",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("접근 토큰(Access Token)")
                        ),
                        pathParameters(
                                parameterWithName("userId").description("퀴즈 작성자 ID"),
                                parameterWithName("quizId").description("삭제할 퀴즈 ID")
                        )));

    }

    @DisplayName("유저 답변 목록 조회 API 테스트 - 정상 호출")
    @Test
    void getMyAnswers_success() throws Exception {

        // given
        AccessToken accessToken = AccessToken.of(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0IjoxNjc1MjEwODc5LCJleHAiOjE2NzY0MjA0NzksInVzZXJJZCI6MX0.Fae1uwS2RPmSad_Uf7pWA8lNqW-MZtm6wP-MDIHwnp8dQpKgaDms3URZBnAG53V8uU-J1Tl0wPFVR6j5wIQS_Q",
                900);

        long userId = 1L;
        given(tokenService.extractUserInfo(any(String.class)))
                .willReturn(UserInfo.of(userId, Role.USER));

        List<MyAnswerDto> content = List.of(
                new MyAnswerDto(1L, "HTTP 프로토콜은 무상태성, 서버-클라이언트 구조, 캐시 가능의 특징을 가집니다.", 3L, "test1@gmail.com", 5, LocalDateTime.now(), LocalDateTime.now()),
                new MyAnswerDto(2L, "저는 모르겠네요; 다른 분이 알려주세요", 4L, "test1@gmail.com", 0, LocalDateTime.now(), LocalDateTime.now()),
                new MyAnswerDto(3L, "test2번 분 답변 감사합니다.", 1L, "test1@gmail.com", 2, LocalDateTime.now(), LocalDateTime.now())
        );

        int totalElements = 1125;
        int totalPages = 12;
        boolean last = false;
        boolean first = true;
        boolean empty = false;

        given(userProfileService.getMyAnswers(any(Pageable.class), any(UserInfo.class)))
                .willReturn(new PagingResponseDto<>(content, totalElements, totalPages, last, first, empty));

        // when
        ResultActions result = mvc.perform(
                get("/api/users/{userId}/answers?page={page}&size={size}&sort={property,direction}", userId, 0, 3, "id,asc")
                        .header(HttpHeaders.AUTHORIZATION, accessToken.grantType() + " " + accessToken.accessToken())
                        .accept(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].contents").value("HTTP 프로토콜은 무상태성, 서버-클라이언트 구조, 캐시 가능의 특징을 가집니다."))
                .andExpect(jsonPath("$.content[0].quizId").value(3L))
                .andExpect(jsonPath("$.content[0].author").value("test1@gmail.com"))
                .andExpect(jsonPath("$.content[0].votes").value(5))
                .andExpect(jsonPath("$.content[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.content[0].modifiedAt").isNotEmpty())
                .andExpect(jsonPath("$.totalElements").value(totalElements))
                .andExpect(jsonPath("$.totalPages").value(totalPages))
                .andExpect(jsonPath("$.last").value(last))
                .andExpect(jsonPath("$.first").value(first))
                .andExpect(jsonPath("$.empty").value(empty));

        then(tokenService).should().extractUserInfo(any(String.class));
        then(userProfileService).should().getMyAnswers(any(Pageable.class), any(UserInfo.class));

        // API 문서화
        result
                .andDo(document("user-profile/get-my-answers",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("접근 토큰(Access Token)")
                        ),
                        pathParameters(
                                parameterWithName("userId").description("유저 ID (PK값)")
                        ),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호 (0부터 시작)"),
                                parameterWithName("size").description("페이지 당 사이즈"),
                                parameterWithName("sort").description("{정렬 기준, 정렬 방향}")
                        ),
                        responseFields(
                                fieldWithPath("content").description("답변 목록"),
                                fieldWithPath("content[].id").description("답변 ID (PK값)"),
                                fieldWithPath("content[].contents").description("답변 내용"),
                                fieldWithPath("content[].quizId").description("이 답변의 퀴즈 ID (PK값)"),
                                fieldWithPath("content[].author").description("답변 작성자"),
                                fieldWithPath("content[].votes").description("이 답변이 얻은 추천 수 (음수도 가능)"),
                                fieldWithPath("content[].createdAt").description("답변 작성 시간"),
                                fieldWithPath("content[].modifiedAt").description("마지막 답변 수정 시간"),
                                fieldWithPath("totalElements").description("전체 답변 수"),
                                fieldWithPath("totalPages").description("전체 페이지 수"),
                                fieldWithPath("last").description("마지막 페이지 여부"),
                                fieldWithPath("first").description("첫 페이지 여부"),
                                fieldWithPath("empty").description("빈 페이지 여부"))));

    }

    @DisplayName("유저 답변 수정 API 테스트 - 정상 호출")
    @Test
    void editMyAnswer_success() throws Exception {

        // given
        AccessToken accessToken = AccessToken.of(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0IjoxNjc1MjEwODc5LCJleHAiOjE2NzY0MjA0NzksInVzZXJJZCI6MX0.Fae1uwS2RPmSad_Uf7pWA8lNqW-MZtm6wP-MDIHwnp8dQpKgaDms3URZBnAG53V8uU-J1Tl0wPFVR6j5wIQS_Q",
                900);

        long answerWriterId = 1L;
        long editAnswerId = 1L;
        given(tokenService.extractUserInfo(any(String.class)))
                .willReturn(UserInfo.of(answerWriterId, Role.USER));

        given(userProfileService.editMyAnswer(any(EditMyAnswerDto.Request.class), any(UserInfo.class), anyLong()))
                .willReturn(EditMyAnswerDto.Response.from(editAnswerId));

        EditMyAnswerDto.Request requestBody = EditMyAnswerDto.Request.builder()
                .contents("수정된 퀴즈 내용, test1 유저가 작성했음")
                .build();

        // when
        ResultActions result = mvc.perform(
                put("/api/users/{userId}/answers/{answerId}", answerWriterId, editAnswerId)
                        .header(HttpHeaders.AUTHORIZATION, accessToken.grantType() + " " + accessToken.accessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.editAnswerId").value(editAnswerId));

        then(tokenService).should().extractUserInfo(any(String.class));
        then(userProfileService).should().editMyAnswer(any(EditMyAnswerDto.Request.class), any(UserInfo.class), anyLong());

        // API 문서화
        result
                .andDo(document("user-profile/edit-my-answer",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("접근 토큰(Access Token)")
                        ),
                        pathParameters(
                                parameterWithName("userId").description("답변 작성자 ID"),
                                parameterWithName("answerId").description("수정할 답변 ID")
                        ),
                        requestFields(
                                fieldWithPath("contents").description("답변 내용").attributes(RestDocsConfiguration.field("constraints", "10자 이상"))
                        ),
                        responseFields(
                                fieldWithPath("editAnswerId").description("수정된 답변 ID"))));

    }

    @DisplayName("유저 답변 삭제 API 테스트 - 정상 호출")
    @Test
    void deleteMyAnswer_success() throws Exception {

        // given
        AccessToken accessToken = AccessToken.of(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0IjoxNjc1MjEwODc5LCJleHAiOjE2NzY0MjA0NzksInVzZXJJZCI6MX0.Fae1uwS2RPmSad_Uf7pWA8lNqW-MZtm6wP-MDIHwnp8dQpKgaDms3URZBnAG53V8uU-J1Tl0wPFVR6j5wIQS_Q",
                900);

        long answerWriterId = 1L;
        long deleteAnswerId = 1L;
        given(tokenService.extractUserInfo(any(String.class)))
                .willReturn(UserInfo.of(answerWriterId, Role.USER));

        willDoNothing().given(userProfileService).deleteMyAnswer(anyLong(), any(UserInfo.class));

        // when
        ResultActions result = mvc.perform(
                delete("/api/users/{userId}/answers/{answerId}", answerWriterId, deleteAnswerId)
                        .header(HttpHeaders.AUTHORIZATION, accessToken.grantType() + " " + accessToken.accessToken())
                        .accept(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(status().isNoContent());

        then(tokenService).should().extractUserInfo(accessToken.accessToken());
        then(userProfileService).should().deleteMyAnswer(deleteAnswerId, UserInfo.of(answerWriterId, Role.USER));

        // API 문서화
        result
                .andDo(document("user-profile/delete-my-answer",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("접근 토큰(Access Token)")
                        ),
                        pathParameters(
                                parameterWithName("userId").description("답변 작성자 ID"),
                                parameterWithName("answerId").description("삭제할 답변 ID")
                        )));

    }
}