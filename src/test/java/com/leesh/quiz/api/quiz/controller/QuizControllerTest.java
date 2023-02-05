package com.leesh.quiz.api.quiz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leesh.quiz.api.quiz.dto.quiz.CreateQuizDto;
import com.leesh.quiz.api.quiz.dto.quiz.QuizDetailDto;
import com.leesh.quiz.api.quiz.dto.quiz.QuizDto;
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
import java.util.Date;
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
                new Date());

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
                new QuizDto(1L, "HTTP 프로토콜의 특징은 무엇인가요?", 5, "test1@gmail.com", 12, LocalDateTime.now(), LocalDateTime.now()),
                new QuizDto(2L, "테스트 5 유저가 작성한 퀴즈", 0, "test5@gmail.com", 3, LocalDateTime.now(), LocalDateTime.now()),
                new QuizDto(3L, "HTTP, HTTPS의 차이점에 대해...", 2, "leesh", 2, LocalDateTime.now(), LocalDateTime.now())
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
                .andExpect(jsonPath("$.content[0].author").value("test1@gmail.com"))
                .andExpect(jsonPath("$.content[0].votes").value(12))
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
                                fieldWithPath("content[].votes").description("이 퀴즈가 얻은 추천 수 (음수도 가능)"),
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

        int totalElements = 1125;
        int totalPages = 12;
        boolean last = false;
        boolean first = true;
        boolean empty = false;

        long quizId = 1L;
        given(quizService.getQuizDetail(anyLong()))
                .willReturn(new QuizDetailDto(
                        quizId,
                        "스프링의 빈 주입 방식에 대해...",
                        "스프링이 빈 주입을 하는 방법에 대해 모두 설명해주세요. 그리고, 우리가 흔히 사용하는 방식은 무엇이며, 각각 어떤 장점이 있는지에 대해 설명해주세요.",
                        answers,
                        "leesh@gmail.com",
                        10,
                        LocalDateTime.now(), LocalDateTime.now()));

        answers.add(new AnswerDto(1L, "먼저, 빈 주입 방식과 수정자 주입 방식이 있습니다. 빈 주입 방식은 @Autowired를 통해 주입하는 방식을 말합니다.", "test2@gmail.com", 1, LocalDateTime.now(), LocalDateTime.now()));
        answers.add(new AnswerDto(2L, "한 가지가 더 있는듯 합니다.", "test3@gmail.com", -2, LocalDateTime.now(), LocalDateTime.now()));
        answers.add(new AnswerDto(2L, "생성자 주입 방식이 있습니다. 불변성을 유지할 수 있기 때문에 주로 생성자 주입 방식을 사용합니다.", "test5@gmail.com", 5, LocalDateTime.now(), LocalDateTime.now()));


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
                .andExpect(jsonPath("$.author").value("leesh@gmail.com"))
                .andExpect(jsonPath("$.votes").value(10))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.modifiedAt").isNotEmpty())
                .andExpect(jsonPath("$.answers[0].id").value(1L))
                .andExpect(jsonPath("$.answers[0].contents").value("먼저, 빈 주입 방식과 수정자 주입 방식이 있습니다. 빈 주입 방식은 @Autowired를 통해 주입하는 방식을 말합니다."))
                .andExpect(jsonPath("$.answers[0].author").value("test2@gmail.com"))
                .andExpect(jsonPath("$.answers[0].votes").value(1))
                .andExpect(jsonPath("$.answers[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.answers[0].modifiedAt").isNotEmpty());

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
                                fieldWithPath("votes").description("퀴즈 추천 수"),
                                fieldWithPath("createdAt").description("퀴즈 생성 시간"),
                                fieldWithPath("modifiedAt").description("퀴즈 수정 시간"),
                                fieldWithPath("answers").description("퀴즈에 달린 답변 목록"),
                                fieldWithPath("answers[].id").description("답변 ID"),
                                fieldWithPath("answers[].contents").description("답변 내용"),
                                fieldWithPath("answers[].author").description("답변 작성자"),
                                fieldWithPath("answers[].votes").description("답변 추천 수"),
                                fieldWithPath("answers[].createdAt").description("답변 생성 시간"),
                                fieldWithPath("answers[].modifiedAt").description("답변 수정 시간")
                        )));

    }


}