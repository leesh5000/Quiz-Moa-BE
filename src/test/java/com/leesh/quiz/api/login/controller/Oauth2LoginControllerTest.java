package com.leesh.quiz.api.login.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leesh.quiz.api.login.dto.Oauth2LoginDto;
import com.leesh.quiz.api.login.service.Oauth2LoginService;
import com.leesh.quiz.domain.user.constant.Oauth2Type;
import com.leesh.quiz.global.jwt.constant.GrantType;
import com.leesh.quiz.global.jwt.dto.AccessToken;
import com.leesh.quiz.global.jwt.dto.RefreshToken;
import com.leesh.quiz.testconfiguration.RestDocsConfiguration;
import com.leesh.quiz.testconfiguration.MvcTestConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Oauth2 Login API 테스트")
@WebMvcTest(Oauth2LoginController.class)
@Import(MvcTestConfiguration.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs
class Oauth2LoginControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private Oauth2LoginService service;

    @DisplayName("정상 호출")
    @Test
    void success_test() throws Exception {

        // given
        Oauth2LoginDto.Request request = new Oauth2LoginDto.Request(
                Oauth2Type.NAVER.name(),
                "y7iyuzOxjD3AnPOtNDkxlKhVEtdjIBduM7uJboWnDskFxrD9GvitLQpqpnA7fAc4pMvowAo9dJcAAAGGCllssw");

        given(service.oauth2Login(request))
                .willReturn(
                        Oauth2LoginDto.Response.from(
                                AccessToken.of("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBQ0NFU1MiLCJpYXQiOjE2NzUyMTA4NzksImV4cCI6MTY3NTIxMTc3OSwidXNlcklkIjoxLCJyb2xlIjoiVVNFUiJ9.X1AfxGWGUPhC5ovt3hcLv8_6Zb8H0Z4yn8tDxHohrTx_kcgTDWIHPt8yDuTHYo9KmqqqIwTQ7VEtMaVyJdqKrQ", new Date()),
                                RefreshToken.of("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0IjoxNjc1MjEwODc5LCJleHAiOjE2NzY0MjA0NzksInVzZXJJZCI6MX0.Fae1uwS2RPmSad_Uf7pWA8lNqW-MZtm6wP-MDIHwnp8dQpKgaDms3URZBnAG53V8uU-J1Tl0wPFVR6j5wIQS_Q", new Date())
                        ));

        // when
        ResultActions result = mvc.perform(post("/api/oauth2/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grantType").value(GrantType.BEARER.getType()))
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.accessTokenExpiresIn").exists())
                .andExpect(jsonPath("$.refreshTokenExpiresIn").exists())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
        ;

        then(service).should().oauth2Login(any(Oauth2LoginDto.Request.class));

        // API 문서화
        result
                .andDo(document("oauth2-login",
                        requestFields(
                                fieldWithPath("oauth2Type").type(JsonFieldType.STRING).description("Oauth2 제공자 타입").attributes(RestDocsConfiguration.field("constraints", "Naver, Kakao, Google 중 하나")),
                                fieldWithPath("authorizationCode").type(JsonFieldType.STRING).description("Oauth2 제공자로부터 받은 인가코드")
                        ),
                        responseFields(
                                fieldWithPath("grantType").type(JsonFieldType.STRING).description(GrantType.BEARER.getType()),
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("Access Token"),
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("Refresh Token"),
                                fieldWithPath("accessTokenExpiresIn").type(JsonFieldType.STRING).description("Access Token 만료 시간"),
                                fieldWithPath("refreshTokenExpiresIn").type(JsonFieldType.STRING).description("Refresh Token 만료 시간")
                        )));

    }

}