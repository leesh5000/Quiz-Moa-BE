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

@DisplayName("?????? API ?????????")
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

    @DisplayName("?????? ?????? API ????????? - ?????? ??????")
    @Test
    void createQuiz_success() throws Exception {

        // given
        AccessToken accessToken = AccessToken.of(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0IjoxNjc1MjEwODc5LCJleHAiOjE2NzY0MjA0NzksInVzZXJJZCI6MX0.Fae1uwS2RPmSad_Uf7pWA8lNqW-MZtm6wP-MDIHwnp8dQpKgaDms3URZBnAG53V8uU-J1Tl0wPFVR6j5wIQS_Q",
                900);

        long userId = 1L;

        UserInfo user = UserInfo.builder()
                .userId(userId)
                .email("test1@gmail.com")
                .username("test1")
                .role(Role.USER)
                .build();

        given(tokenService.extractUserInfo(any(String.class)))
                .willReturn(user);

        long createQuizId = 1L;
        CreateQuizDto.Request request = new CreateQuizDto.Request("????????? ?????????????????? ????????? ?????? ???????????????.", "???????????????, ????????? ??? ??? ?????? ??????????????? ????????????.");
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

        // API ?????????
        result
                .andDo(document("quiz/create-quiz",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("?????? ?????? (Access Token)")
                        ),
                        requestFields(
                                fieldWithPath("title").description("?????? ??????").attributes(RestDocsConfiguration.field("constraints", "10??? ?????? 255??? ??????")),
                                fieldWithPath("contents").description("?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("createQuizId").description("????????? ????????? ID")
                        )));

    }

    @DisplayName("?????? ?????? ?????? API ????????? - ?????? ??????")
    @Test
    void getQuizzes_success() throws Exception {

        // given
        List<QuizDto> content = List.of(
                QuizDto.builder()
                        .id(1L)
                        .title("HTTP ??????????????? ????????? ????????????????")
                        .answerCount(5)
                        .totalVotesSum(12)
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
                        .title("????????? 5 ????????? ????????? ??????")
                        .answerCount(0)
                        .totalVotesSum(3)
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
                .andExpect(jsonPath("$.content[0].title").value("HTTP ??????????????? ????????? ????????????????"))
                .andExpect(jsonPath("$.content[0].answerCount").value(5))
                .andExpect(jsonPath("$.content[0].author").isNotEmpty())
                .andExpect(jsonPath("$.content[0].author.id").value(1L))
                .andExpect(jsonPath("$.content[0].author.email").value("test1@gmail.com"))
                .andExpect(jsonPath("$.content[0].author.username").value("test1"))
                .andExpect(jsonPath("$.content[0].totalVotesSum").value(12))
                .andExpect(jsonPath("$.content[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.content[0].modifiedAt").isNotEmpty())
                .andExpect(jsonPath("$.totalElements").value(totalElements))
                .andExpect(jsonPath("$.totalPages").value(totalPages))
                .andExpect(jsonPath("$.last").value(last))
                .andExpect(jsonPath("$.first").value(first))
                .andExpect(jsonPath("$.empty").value(empty));

        then(quizService).should().getQuizzesByPaging(any(Pageable.class));

        // API ?????????
        result
                .andDo(document("quiz/get-quizzes",
                        queryParameters(
                                parameterWithName("page").description("????????? ?????? (0?????? ??????)"),
                                parameterWithName("size").description("????????? ??? ?????????"),
                                parameterWithName("sort").description("{?????? ??????, ?????? ??????}")
                        ),
                        responseFields(
                                fieldWithPath("content").description("?????? ??????"),
                                fieldWithPath("content[].id").description("?????? ID (PK???)"),
                                fieldWithPath("content[].title").description("?????? ??????"),
                                fieldWithPath("content[].answerCount").description("?????? ?????? ???"),
                                fieldWithPath("content[].author").description("?????? ?????????"),
                                fieldWithPath("content[].author.id").description("?????? ????????? ID (PK???)"),
                                fieldWithPath("content[].author.email").description("?????? ????????? ?????????"),
                                fieldWithPath("content[].author.username").description("?????? ????????? ??????"),
                                fieldWithPath("content[].totalVotesSum").description("????????? ??? - ????????? ???"),
                                fieldWithPath("content[].createdAt").description("?????? ?????? ??????"),
                                fieldWithPath("content[].modifiedAt").description("????????? ?????? ?????? ??????"),
                                fieldWithPath("totalElements").description("?????? ?????? ???"),
                                fieldWithPath("totalPages").description("?????? ????????? ???"),
                                fieldWithPath("last").description("????????? ????????? ??????"),
                                fieldWithPath("first").description("??? ????????? ??????"),
                                fieldWithPath("empty").description("??? ????????? ??????"))));

    }

    @DisplayName("?????? ???????????? ?????? API ????????? - ?????? ??????")
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

        answers.add(new AnswerDto(1L, "??????, ??? ?????? ????????? ????????? ?????? ????????? ????????????. ??? ?????? ????????? @Autowired??? ?????? ???????????? ????????? ????????????.", test1, answerVotes, LocalDateTime.now(), LocalDateTime.now()));
        answers.add(new AnswerDto(2L, "??? ????????? ??? ????????? ?????????.", test2, answerVotes, LocalDateTime.now(), LocalDateTime.now()));
        answers.add(new AnswerDto(3L, "????????? ?????? ????????? ????????????. ???????????? ????????? ??? ?????? ????????? ?????? ????????? ?????? ????????? ???????????????.", test3, answerVotes, LocalDateTime.now(), LocalDateTime.now()));

        long quizId = 1L;
        given(quizService.getQuizDetail(anyLong()))
                .willReturn(new QuizDetailDto(
                        quizId,
                        "???????????? ??? ?????? ????????? ??????...",
                        "???????????? ??? ????????? ?????? ????????? ?????? ?????? ??????????????????. ?????????, ????????? ?????? ???????????? ????????? ????????????, ?????? ?????? ????????? ???????????? ?????? ??????????????????.",
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
                .andExpect(jsonPath("$.title").value("???????????? ??? ?????? ????????? ??????..."))
                .andExpect(jsonPath("$.contents").value("???????????? ??? ????????? ?????? ????????? ?????? ?????? ??????????????????. ?????????, ????????? ?????? ???????????? ????????? ????????????, ?????? ?????? ????????? ???????????? ?????? ??????????????????."))
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
                .andExpect(jsonPath("$.answers[0].contents").value("??????, ??? ?????? ????????? ????????? ?????? ????????? ????????????. ??? ?????? ????????? @Autowired??? ?????? ???????????? ????????? ????????????."))
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

        // API ?????????
        result
                .andDo(document("quiz/get-quiz-detail",
                        pathParameters(
                                parameterWithName("quiz-id").description("?????? ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("?????? ID"),
                                fieldWithPath("title").description("?????? ??????"),
                                fieldWithPath("contents").description("?????? ??????"),
                                fieldWithPath("author").description("?????? ?????????"),
                                fieldWithPath("author.id").description("?????? ????????? ID"),
                                fieldWithPath("author.username").description("?????? ????????? ??????"),
                                fieldWithPath("author.email").description("?????? ????????? ?????????"),
                                fieldWithPath("createdAt").description("?????? ?????? ??????"),
                                fieldWithPath("modifiedAt").description("?????? ?????? ??????"),
                                fieldWithPath("votes").description("?????? ????????? ?????? ?????? ?????? (?????? ??????)"),
                                fieldWithPath("votes[].id").description("?????? ID"),
                                fieldWithPath("votes[].value").description("?????? ???"),
                                fieldWithPath("votes[].voter").description("?????????"),
                                fieldWithPath("votes[].voter.id").description("????????? ID"),
                                fieldWithPath("votes[].voter.username").description("????????? ??????"),
                                fieldWithPath("votes[].voter.email").description("????????? ?????????"),
                                fieldWithPath("answers").description("????????? ?????? ?????? ??????"),
                                fieldWithPath("answers[].id").description("?????? ID"),
                                fieldWithPath("answers[].contents").description("?????? ??????"),
                                fieldWithPath("answers[].author").description("?????? ?????????"),
                                fieldWithPath("answers[].author.id").description("?????? ????????? ID"),
                                fieldWithPath("answers[].author.username").description("?????? ????????? ??????"),
                                fieldWithPath("answers[].author.email").description("?????? ????????? ?????????"),
                                fieldWithPath("answers[].createdAt").description("?????? ?????? ??????"),
                                fieldWithPath("answers[].modifiedAt").description("?????? ?????? ??????"),
                                fieldWithPath("answers[].votes").description("????????? ?????? ?????? ??????"),
                                fieldWithPath("answers[].votes[].id").description("?????? ID"),
                                fieldWithPath("answers[].votes[].value").description("?????? ???"),
                                fieldWithPath("answers[].votes[].voter").description("?????????"),
                                fieldWithPath("answers[].votes[].voter.id").description("????????? ID"),
                                fieldWithPath("answers[].votes[].voter.username").description("????????? ??????"),
                                fieldWithPath("answers[].votes[].voter.email").description("????????? ?????????")
                        )));

    }

    @DisplayName("????????? ?????? ?????? API ????????? - ?????? ??????")
    @Test
    void createAnswer_success() throws Exception {

        // given
        AccessToken accessToken = AccessToken.of(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0IjoxNjc1MjEwODc5LCJleHAiOjE2NzY0MjA0NzksInVzZXJJZCI6MX0.Fae1uwS2RPmSad_Uf7pWA8lNqW-MZtm6wP-MDIHwnp8dQpKgaDms3URZBnAG53V8uU-J1Tl0wPFVR6j5wIQS_Q",
                900);

        long userId = 1L;

        UserInfo user = UserInfo.builder()
                .userId(userId)
                .email("test1@gmail.com")
                .username("test1")
                .role(Role.USER)
                .build();

        given(tokenService.extractUserInfo(any(String.class)))
                .willReturn(user);

        long createAnswerId = 1L;
        CreateAnswerDto.Request request = new CreateAnswerDto.Request("????????? ??????????????? ????????? ???????????? ???????????? ?????? ????????? ????????? ????????? ????????? ???????????? ??????????????????, ?????????????????? ?????? ????????? ????????? ???????????? ???????????? ?????????????????????.");
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

        // API ?????????
        result
                .andDo(document("quiz/create-answer",
                        pathParameters(
                                parameterWithName("quiz-id").description("?????? ID")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("?????? ?????? (Access Token)")
                        ),
                        requestFields(
                                fieldWithPath("contents").description("?????? ??????").attributes(RestDocsConfiguration.field("constraints", "10??? ?????? 255??? ??????"))
                        ),
                        responseFields(
                                fieldWithPath("createAnswerId").description("????????? ????????? ID")
                        )));

    }

    @DisplayName("????????? ?????? ?????? API ????????? - ?????? ??????")
    @Test
    void createQuizVote_success() throws Exception {

        // given
        AccessToken accessToken = AccessToken.of(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0IjoxNjc1MjEwODc5LCJleHAiOjE2NzY0MjA0NzksInVzZXJJZCI6MX0.Fae1uwS2RPmSad_Uf7pWA8lNqW-MZtm6wP-MDIHwnp8dQpKgaDms3URZBnAG53V8uU-J1Tl0wPFVR6j5wIQS_Q",
                900);

        long userId = 1L;

        UserInfo user = UserInfo.builder()
                .userId(userId)
                .email("test1@gmail.com")
                .username("test1")
                .role(Role.USER)
                .build();

        given(tokenService.extractUserInfo(any(String.class)))
                .willReturn(user);

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

        // API ?????????
        result
                .andDo(document("quiz/create-quiz-vote",
                        pathParameters(
                                parameterWithName("quiz-id").description("????????? ?????? ID")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("?????? ?????? (Access Token)")
                        ),
                        requestFields(
                                fieldWithPath("value").description("?????? ???").attributes(RestDocsConfiguration.field("constraints", "1 ?????? -1"))
                        ),
                        responseFields(
                                fieldWithPath("createdVoteId").description("????????? ????????? ID")
                        )));

    }

}