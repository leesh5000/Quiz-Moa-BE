package com.leesh.quiz.api.quiz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leesh.quiz.api.quiz.dto.quiz.CreateQuizDto;
import com.leesh.quiz.api.quiz.service.QuizService;
import com.leesh.quiz.domain.user.constant.Role;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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


}