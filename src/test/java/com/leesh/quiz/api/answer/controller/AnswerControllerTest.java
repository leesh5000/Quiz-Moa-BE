package com.leesh.quiz.api.answer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leesh.quiz.api.answer.dto.CreateAnswerVoteDto;
import com.leesh.quiz.api.answer.service.AnswerService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("?????? API ?????????")
@WebMvcTest(AnswerController.class)
@Import(MvcTestConfiguration.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs
class AnswerControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private AnswerService answerService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("????????? ?????? ?????? API ????????? - ?????? ??????")
    @Test
    void createAnswerVote_success() throws Exception {

        // given
        AccessToken accessToken = AccessToken.of(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0IjoxNjc1MjEwODc5LCJleHAiOjE2NzY0MjA0NzksInVzZXJJZCI6MX0.Fae1uwS2RPmSad_Uf7pWA8lNqW-MZtm6wP-MDIHwnp8dQpKgaDms3URZBnAG53V8uU-J1Tl0wPFVR6j5wIQS_Q",
                900);

        long userId = 1L;
        given(tokenService.extractUserInfo(any(String.class)))
                .willReturn(
                        UserInfo.builder()
                                .userId(userId)
                                .email("test1@gmail.com")
                                .username("test1")
                                .role(Role.USER)
                                .build()
                );

        long createAnswerVoteId = 1L;
        CreateAnswerVoteDto.Request request = new CreateAnswerVoteDto.Request(Byte.valueOf("1"));
        CreateAnswerVoteDto.Response response = CreateAnswerVoteDto.Response.from(createAnswerVoteId);

        given(answerService.createAnswerVote(any(UserInfo.class), anyLong(), any(CreateAnswerVoteDto.Request.class)))
                .willReturn(response);

        // when
        ResultActions result = mvc.perform(
                post("/api/answers/{answer-id}/votes", createAnswerVoteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, accessToken.grantType() + " " + accessToken.accessToken())
                        .content(objectMapper.writeValueAsString(request)
                        ));

        // then
        result
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.createdVoteId").value(createAnswerVoteId));

        then(answerService).should().createAnswerVote(any(UserInfo.class), anyLong(), any(CreateAnswerVoteDto.Request.class));
        then(tokenService).should().extractUserInfo(any(String.class));

        // API ?????????
        result
                .andDo(document("answer/create-answer-vote",
                        pathParameters(
                                parameterWithName("answer-id").description("????????? ?????? ID")
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