package com.leesh.quiz.api.userprofile.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leesh.quiz.api.userprofile.dto.EditMyQuizDto;
import com.leesh.quiz.api.userprofile.dto.MyQuizDto;
import com.leesh.quiz.api.userprofile.dto.PagingResponseDto;
import com.leesh.quiz.api.userprofile.service.UserProfileService;
import com.leesh.quiz.domain.user.constant.Role;
import com.leesh.quiz.global.constant.UserInfo;
import com.leesh.quiz.global.jwt.dto.AccessToken;
import com.leesh.quiz.global.jwt.service.TokenService;
import com.leesh.quiz.testconfiguration.MvcTestConfiguration;
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
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
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

    @DisplayName("퀴즈 목록 조회 API 테스트 - 정상 호출")
    @Test
    void getMyQuizzes_success() throws Exception {

        // given
        AccessToken accessToken = AccessToken.of(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0IjoxNjc1MjEwODc5LCJleHAiOjE2NzY0MjA0NzksInVzZXJJZCI6MX0.Fae1uwS2RPmSad_Uf7pWA8lNqW-MZtm6wP-MDIHwnp8dQpKgaDms3URZBnAG53V8uU-J1Tl0wPFVR6j5wIQS_Q",
                new Date());

        long userId = 1L;
        given(tokenService.extractUserInfo(any(String.class)))
                .willReturn(UserInfo.of(userId, Role.USER));

        List<MyQuizDto> content = List.of(
                new MyQuizDto(1L, "test1의 퀴즈", "HTTP 프로토콜의 특징은 무엇인가요?", 5, "test1@gmail.com", 12, LocalDateTime.now(), LocalDateTime.now()),
                new MyQuizDto(2L, "test2의 퀴즈", "웹 서버와 웹 애플리케이션의 차이는?", 0, "test2", 3, LocalDateTime.now(), LocalDateTime.now()),
                new MyQuizDto(3L, "test3의 퀴즈", "서버사이드 렌더링과 클라이언트 사이드 렌더링의 차이는?", 2, "leesh5000", 2, LocalDateTime.now(), LocalDateTime.now())
                );

        int totalElements = 1125;
        int totalPages = 12;
        boolean last = false;
        boolean first = true;
        boolean empty = false;

        given(userProfileService.getMyQuizzes(any(Pageable.class), any(UserInfo.class)))
                .willReturn(new PagingResponseDto(content, totalElements, totalPages, last, first, empty));

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
                .andExpect(jsonPath("$.content[0].title").value("test1의 퀴즈"))
                .andExpect(jsonPath("$.content[0].contents").value("HTTP 프로토콜의 특징은 무엇인가요?"))
                .andExpect(jsonPath("$.content[0].answerCount").value(5))
                .andExpect(jsonPath("$.content[0].author").value("test1@gmail.com"))
                .andExpect(jsonPath("$.content[0].votes").value(12))
                .andExpect(jsonPath("$.content[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.content[0].modifiedAt").isNotEmpty())
                .andExpect(jsonPath("$.totalElements").value(totalElements))
                .andExpect(jsonPath("$.totalPages").value(totalPages))
                .andExpect(jsonPath("$.last").value(last))
                .andExpect(jsonPath("$.first").value(first))
                .andExpect(jsonPath("$.empty").value(empty));

        then(tokenService).should().extractUserInfo(any(String.class));
        then(userProfileService).should().getMyQuizzes(any(Pageable.class), any(UserInfo.class));

        // API 문서화
        result
                .andDo(document("user-profile/get-quizzes",
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
                                fieldWithPath("content").description("퀴즈 목록"),
                                fieldWithPath("content[].id").description("퀴즈 ID (PK값)"),
                                fieldWithPath("content[].title").description("퀴즈 제목"),
                                fieldWithPath("content[].contents").description("퀴즈 내용"),
                                fieldWithPath("content[].answerCount").description("퀴즈 답변 수"),
                                fieldWithPath("content[].author").description("퀴즈 작성자"),
                                fieldWithPath("content[].votes").description("이 퀴즈가 얻은 추천 수 (음수도 가능)"),
                                fieldWithPath("content[].createdAt").description("퀴즈 작성 시간"),
                                fieldWithPath("content[].modifiedAt").description("마지막 퀴즈 수정 시간"),
                                fieldWithPath("totalElements").description("전체 퀴즈 수"),
                                fieldWithPath("totalPages").description("전체 페이지 수"),
                                fieldWithPath("last").description("마지막 페이지 여부"),
                                fieldWithPath("first").description("첫 페이지 여부"),
                                fieldWithPath("empty").description("빈 페이지 여부"))));

    }

    @DisplayName("퀴즈 수정 API 테스트 - 정상 호출")
    @Test
    void editMyQuiz_success() throws Exception {

        // given
        AccessToken accessToken = AccessToken.of(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0IjoxNjc1MjEwODc5LCJleHAiOjE2NzY0MjA0NzksInVzZXJJZCI6MX0.Fae1uwS2RPmSad_Uf7pWA8lNqW-MZtm6wP-MDIHwnp8dQpKgaDms3URZBnAG53V8uU-J1Tl0wPFVR6j5wIQS_Q",
                new Date());

        long quizWriterId = 1L;
        long editQuizId = 1L;
        given(tokenService.extractUserInfo(any(String.class)))
                .willReturn(UserInfo.of(quizWriterId, Role.USER));

        given(userProfileService.editMyQuiz(any(EditMyQuizDto.Request.class), any(UserInfo.class), anyLong()))
                .willReturn(EditMyQuizDto.Response.from(editQuizId));

        EditMyQuizDto.Request requestBody = EditMyQuizDto.Request.builder()
                .title("수정된 퀴즈 제목")
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
                                fieldWithPath("title").description("퀴즈 제목"),
                                fieldWithPath("contents").description("퀴즈 내용")
                        ),
                        responseFields(
                                fieldWithPath("editQuizId").description("수정된 퀴즈 ID"))));

    }
}