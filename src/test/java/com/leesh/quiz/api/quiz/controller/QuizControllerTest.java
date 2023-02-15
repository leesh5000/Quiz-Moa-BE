package com.leesh.quiz.api.quiz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leesh.quiz.api.quiz.dto.answer.CreateAnswerDto;
import com.leesh.quiz.api.quiz.dto.quiz.CreateQuizDto;
import com.leesh.quiz.api.quiz.dto.quiz.QuizDetailDto;
import com.leesh.quiz.api.quiz.dto.quiz.QuizDto;
import com.leesh.quiz.api.quiz.dto.vote.CreateQuizVoteDto;
import com.leesh.quiz.api.quiz.service.QuizService;
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
import java.util.ArrayList;
import java.util.List;

import static com.leesh.quiz.api.quiz.dto.quiz.QuizDetailDto.AnswerDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("퀴즈 API 테스트")
@WebMvcTest(QuizController.class)
@Import(MvcTestConfiguration.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs
class QuizControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private QuizService quizService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("퀴즈 생성 API 테스트 - 정상 호출")
    @Test
    void createQuiz_success() throws Exception {

        // given
        AccessToken accessToken = AccessToken.of(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0IjoxNjc1MjEwODc5LCJleHAiOjE2NzY0MjA0NzksInVzZXJJZCI6MX0.Fae1uwS2RPmSad_Uf7pWA8lNqW-MZtm6wP-MDIHwnp8dQpKgaDms3URZBnAG53V8uU-J1Tl0wPFVR6j5wIQS_Q",
                900);

        long userId = 1L;
        given(tokenService.extractUserInfo(any(String.class)))
                .willReturn(UserInfo.of(userId, Role.USER));

        long createQuizId = 1L;
        CreateQuizDto.Request request = new CreateQuizDto.Request("배열과 연결리스트의 차이에 대해 설명하시오.", "추가적으로, 스택과 큐 그 외의 자료구조도 좋습니다.");
        CreateQuizDto.Response response = CreateQuizDto.Response.from(createQuizId);

        given(quizService.createQuiz(any(UserInfo.class), any(CreateQuizDto.Request.class)))
                .willReturn(response);

        // when
        ResultActions result = mvc.perform(
                post("/api/quizzes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, accessToken.grantType() + " " + accessToken.accessToken())
                        .content(objectMapper.writeValueAsString(request)
                        ));

        // then
        result
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.createQuizId").value(createQuizId));

        then(quizService).should().createQuiz(any(UserInfo.class), any(CreateQuizDto.Request.class));

        // API 문서화
        result
                .andDo(document("quiz/create-quiz",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("접근 토큰 (Access Token)")
                        ),
                        requestFields(
                                fieldWithPath("title").description("퀴즈 제목").attributes(RestDocsConfiguration.field("constraints", "10자 이상 255자 이하")),
                                fieldWithPath("contents").description("퀴즈 내용")
                        ),
                        responseFields(
                                fieldWithPath("createQuizId").description("생성된 퀴즈의 ID")
                        )));

    }

    @DisplayName("퀴즈 목록 조회 API 테스트 - 정상 호출")
    @Test
    void getQuizzes_success() throws Exception {

        // given
        List<QuizDto> content = List.of(
                QuizDto.builder()
                        .id(1L)
                        .title("HTTP 프로토콜의 특징은 무엇인가요?")
                        .answerCount(5)
                        .totalVotes(12)
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .author(
                                QuizDto.Author.builder()
                                        .id(1L)
                                        .email("test1@gmail.com")
                                        .username("test1")
                                        .build())
                        .build(),
                QuizDto.builder()
                        .id(2L)
                        .title("테스트 5 유저가 작성한 퀴즈")
                        .answerCount(0)
                        .totalVotes(3)
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .author(
                                QuizDto.Author.builder()
                                        .id(5L)
                                        .email("test5@gmail.com")
                                        .username("test5")
                                        .build())
                        .build()
        );

        int totalElements = 1125;
        int totalPages = 12;
        boolean last = false;
        boolean first = true;
        boolean empty = false;

        given(quizService.getQuizzesByPaging(any(Pageable.class)))
                .willReturn(new PagingResponseDto<>(content, totalElements, totalPages, last, first, empty));

        // when
        ResultActions result = mvc.perform(
                get("/api/quizzes?page={page}&size={size}&sort={property,direction}", 0, 3, "id,asc")
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

        then(quizService).should().getQuizzesByPaging(any(Pageable.class));

        // API 문서화
        result
                .andDo(document("quiz/get-quizzes",
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

    @DisplayName("퀴즈 세부내용 조회 API 테스트 - 정상 호출")
    @Test
    void getQuizDetail_success() throws Exception {

        // given
        List<AnswerDto> answers = new ArrayList<>();

        QuizDetailDto.AuthorDto test1 = new QuizDetailDto.AuthorDto(1L, "test1", "test1@gmail.com");
        QuizDetailDto.AuthorDto test2 = new QuizDetailDto.AuthorDto(2L, "test2", "test2@gmail.com");
        QuizDetailDto.AuthorDto test3 = new QuizDetailDto.AuthorDto(3L, "test3", "test3@gmail.com");

        List<QuizDetailDto.AnswerVoteDto> answerVotes = new ArrayList<>();
        answerVotes.add(new QuizDetailDto.AnswerVoteDto(1L, 1, test1));
        answerVotes.add(new QuizDetailDto.AnswerVoteDto(2L, 1, test2));
        answerVotes.add(new QuizDetailDto.AnswerVoteDto(3L, 1, test3));

        List<QuizDetailDto.QuizVoteDto> quizVotes = new ArrayList<>();
        quizVotes.add(new QuizDetailDto.QuizVoteDto(1L, 1, test1));
        quizVotes.add(new QuizDetailDto.QuizVoteDto(2L, 1, test2));
        quizVotes.add(new QuizDetailDto.QuizVoteDto(3L, 1, test3));

        answers.add(new AnswerDto(1L, "먼저, 빈 주입 방식과 수정자 주입 방식이 있습니다. 빈 주입 방식은 @Autowired를 통해 주입하는 방식을 말합니다.", test1, answerVotes, LocalDateTime.now(), LocalDateTime.now()));
        answers.add(new AnswerDto(2L, "한 가지가 더 있는듯 합니다.", test2, answerVotes, LocalDateTime.now(), LocalDateTime.now()));
        answers.add(new AnswerDto(3L, "생성자 주입 방식이 있습니다. 불변성을 유지할 수 있기 때문에 주로 생성자 주입 방식을 사용합니다.", test3, answerVotes, LocalDateTime.now(), LocalDateTime.now()));

        long quizId = 1L;
        given(quizService.getQuizDetail(anyLong()))
                .willReturn(new QuizDetailDto(
                        quizId,
                        "스프링의 빈 주입 방식에 대해...",
                        "스프링이 빈 주입을 하는 방법에 대해 모두 설명해주세요. 그리고, 우리가 흔히 사용하는 방식은 무엇이며, 각각 어떤 장점이 있는지에 대해 설명해주세요.",
                        test1,
                        answers, quizVotes,
                        LocalDateTime.now(), LocalDateTime.now()));

        // when
        ResultActions result = mvc.perform(
                get("/api/quizzes/{quiz-id}", quizId)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(quizId))
                .andExpect(jsonPath("$.title").value("스프링의 빈 주입 방식에 대해..."))
                .andExpect(jsonPath("$.contents").value("스프링이 빈 주입을 하는 방법에 대해 모두 설명해주세요. 그리고, 우리가 흔히 사용하는 방식은 무엇이며, 각각 어떤 장점이 있는지에 대해 설명해주세요."))
                .andExpect(jsonPath("$.author").isNotEmpty())
                .andExpect(jsonPath("$.author.id").value(1L))
                .andExpect(jsonPath("$.author.username").value("test1"))
                .andExpect(jsonPath("$.author.email").value("test1@gmail.com"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.modifiedAt").isNotEmpty())
                .andExpect(jsonPath("$.votes").isArray())
                .andExpect(jsonPath("$.votes[0].id").value(1L))
                .andExpect(jsonPath("$.votes[0].value").value(1))
                .andExpect(jsonPath("$.votes[0].voter").isNotEmpty())
                .andExpect(jsonPath("$.votes[0].voter.id").value(1L))
                .andExpect(jsonPath("$.votes[0].voter.username").value("test1"))
                .andExpect(jsonPath("$.votes[0].voter.email").value("test1@gmail.com"))
                .andExpect(jsonPath("$.answers").isArray())
                .andExpect(jsonPath("$.answers[0].id").value(1L))
                .andExpect(jsonPath("$.answers[0].contents").value("먼저, 빈 주입 방식과 수정자 주입 방식이 있습니다. 빈 주입 방식은 @Autowired를 통해 주입하는 방식을 말합니다."))
                .andExpect(jsonPath("$.answers[0].author").isNotEmpty())
                .andExpect(jsonPath("$.answers[0].author.id").value(1L))
                .andExpect(jsonPath("$.answers[0].author.username").value("test1"))
                .andExpect(jsonPath("$.answers[0].author.email").value("test1@gmail.com"))
                .andExpect(jsonPath("$.answers[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.answers[0].modifiedAt").isNotEmpty())
                .andExpect(jsonPath("$.answers[0].votes").isArray())
                .andExpect(jsonPath("$.answers[0].votes[0].id").value(1L))
                .andExpect(jsonPath("$.answers[0].votes[0].value").value(1))
                .andExpect(jsonPath("$.answers[0].votes[0].voter").isNotEmpty())
                .andExpect(jsonPath("$.answers[0].votes[0].voter.id").value(1L))
                .andExpect(jsonPath("$.answers[0].votes[0].voter.username").value("test1"))
                .andExpect(jsonPath("$.answers[0].votes[0].voter.email").value("test1@gmail.com"));

        then(quizService).should().getQuizDetail(anyLong());

        // API 문서화
        result
                .andDo(document("quiz/get-quiz-detail",
                        pathParameters(
                                parameterWithName("quiz-id").description("퀴즈 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("퀴즈 ID"),
                                fieldWithPath("title").description("퀴즈 제목"),
                                fieldWithPath("contents").description("퀴즈 내용"),
                                fieldWithPath("author").description("퀴즈 작성자"),
                                fieldWithPath("author.id").description("퀴즈 작성자 ID"),
                                fieldWithPath("author.username").description("퀴즈 작성자 이름"),
                                fieldWithPath("author.email").description("퀴즈 작성자 이메일"),
                                fieldWithPath("createdAt").description("퀴즈 생성 시간"),
                                fieldWithPath("modifiedAt").description("퀴즈 수정 시간"),
                                fieldWithPath("votes").description("현재 퀴즈에 대한 투표 정보 (추천 정보)"),
                                fieldWithPath("votes[].id").description("투표 ID"),
                                fieldWithPath("votes[].value").description("투표 값"),
                                fieldWithPath("votes[].voter").description("투표자"),
                                fieldWithPath("votes[].voter.id").description("투표자 ID"),
                                fieldWithPath("votes[].voter.username").description("투표자 이름"),
                                fieldWithPath("votes[].voter.email").description("투표자 이메일"),
                                fieldWithPath("answers").description("퀴즈에 대한 답변 정보"),
                                fieldWithPath("answers[].id").description("답변 ID"),
                                fieldWithPath("answers[].contents").description("답변 내용"),
                                fieldWithPath("answers[].author").description("답변 작성자"),
                                fieldWithPath("answers[].author.id").description("답변 작성자 ID"),
                                fieldWithPath("answers[].author.username").description("답변 작성자 이름"),
                                fieldWithPath("answers[].author.email").description("답변 작성자 이메일"),
                                fieldWithPath("answers[].createdAt").description("답변 생성 시간"),
                                fieldWithPath("answers[].modifiedAt").description("답변 수정 시간"),
                                fieldWithPath("answers[].votes").description("답변에 대한 투표 정보"),
                                fieldWithPath("answers[].votes[].id").description("투표 ID"),
                                fieldWithPath("answers[].votes[].value").description("투표 값"),
                                fieldWithPath("answers[].votes[].voter").description("투표자"),
                                fieldWithPath("answers[].votes[].voter.id").description("투표자 ID"),
                                fieldWithPath("answers[].votes[].voter.username").description("투표자 이름"),
                                fieldWithPath("answers[].votes[].voter.email").description("투표자 이메일")
                        )));

    }

    @DisplayName("퀴즈에 답변 달기 API 테스트 - 정상 호출")
    @Test
    void createAnswer_success() throws Exception {

        // given
        AccessToken accessToken = AccessToken.of(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0IjoxNjc1MjEwODc5LCJleHAiOjE2NzY0MjA0NzksInVzZXJJZCI6MX0.Fae1uwS2RPmSad_Uf7pWA8lNqW-MZtm6wP-MDIHwnp8dQpKgaDms3URZBnAG53V8uU-J1Tl0wPFVR6j5wIQS_Q",
                900);

        long userId = 1L;
        given(tokenService.extractUserInfo(any(String.class)))
                .willReturn(UserInfo.of(userId, Role.USER));

        long createAnswerId = 1L;
        CreateAnswerDto.Request request = new CreateAnswerDto.Request("배열은 물리적으로 인접해 위치하며 인덱스로 접근 가능한 동일한 데이터 타입을 저장하는 자료구조이며, 연결리스트는 다음 노드의 주소와 데이터를 저장하는 자료구조입니다.");
        CreateAnswerDto.Response response = CreateAnswerDto.Response.from(createAnswerId);

        given(quizService.createAnswer(any(UserInfo.class), anyLong(), any(CreateAnswerDto.Request.class)))
                .willReturn(response);

        // when
        ResultActions result = mvc.perform(
                post("/api/quizzes/{quiz-id}/answers", createAnswerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, accessToken.grantType() + " " + accessToken.accessToken())
                        .content(objectMapper.writeValueAsString(request)
                        ));

        // then
        result
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.createAnswerId").value(createAnswerId));

        then(quizService).should().createAnswer(any(UserInfo.class), anyLong(), any(CreateAnswerDto.Request.class));
        then(tokenService).should().extractUserInfo(any(String.class));

        // API 문서화
        result
                .andDo(document("quiz/create-answer",
                        pathParameters(
                                parameterWithName("quiz-id").description("퀴즈 ID")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("접근 토큰 (Access Token)")
                        ),
                        requestFields(
                                fieldWithPath("contents").description("답변 내용").attributes(RestDocsConfiguration.field("constraints", "10자 이상 255자 이하"))
                        ),
                        responseFields(
                                fieldWithPath("createAnswerId").description("생성된 답변의 ID")
                        )));

    }

    @DisplayName("퀴즈에 투표 하기 API 테스트 - 정상 호출")
    @Test
    void createQuizVote_success() throws Exception {

        // given
        AccessToken accessToken = AccessToken.of(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0IjoxNjc1MjEwODc5LCJleHAiOjE2NzY0MjA0NzksInVzZXJJZCI6MX0.Fae1uwS2RPmSad_Uf7pWA8lNqW-MZtm6wP-MDIHwnp8dQpKgaDms3URZBnAG53V8uU-J1Tl0wPFVR6j5wIQS_Q",
                900);

        long userId = 1L;
        given(tokenService.extractUserInfo(any(String.class)))
                .willReturn(UserInfo.of(userId, Role.USER));

        long createQuizVoteId = 1L;
        CreateQuizVoteDto.Request request = new CreateQuizVoteDto.Request(Byte.valueOf("1"));
        CreateQuizVoteDto.Response response = CreateQuizVoteDto.Response.from(createQuizVoteId);

        given(quizService.createQuizVote(any(UserInfo.class), anyLong(), any(CreateQuizVoteDto.Request.class)))
                .willReturn(response);

        // when
        ResultActions result = mvc.perform(
                post("/api/quizzes/{quiz-id}/votes", createQuizVoteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, accessToken.grantType() + " " + accessToken.accessToken())
                        .content(objectMapper.writeValueAsString(request)
                        ));

        // then
        result
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.createdVoteId").value(createQuizVoteId));

        then(quizService).should().createQuizVote(any(UserInfo.class), anyLong(), any(CreateQuizVoteDto.Request.class));
        then(tokenService).should().extractUserInfo(any(String.class));

        // API 문서화
        result
                .andDo(document("quiz/create-quiz-vote",
                        pathParameters(
                                parameterWithName("quiz-id").description("투표할 퀴즈 ID")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("접근 토큰 (Access Token)")
                        ),
                        requestFields(
                                fieldWithPath("value").description("투표 값").attributes(RestDocsConfiguration.field("constraints", "1 또는 -1"))
                        ),
                        responseFields(
                                fieldWithPath("createdVoteId").description("생성된 투표의 ID")
                        )));

    }

}