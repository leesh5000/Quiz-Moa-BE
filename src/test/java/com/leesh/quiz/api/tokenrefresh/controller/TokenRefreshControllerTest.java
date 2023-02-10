package com.leesh.quiz.api.tokenrefresh.controller;

import com.leesh.quiz.api.tokenrefresh.dto.TokenRefreshDto;
import com.leesh.quiz.api.tokenrefresh.service.TokenRefreshService;
import com.leesh.quiz.global.jwt.constant.GrantType;
import com.leesh.quiz.global.jwt.dto.AccessToken;
import com.leesh.quiz.global.jwt.dto.RefreshToken;
import com.leesh.quiz.testconfiguration.MvcTestConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("접근 토큰 갱신 API 테스트")
@WebMvcTest(TokenRefreshController.class)
@Import(MvcTestConfiguration.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs
class TokenRefreshControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TokenRefreshService service;

    @DisplayName("정상 호출")
    @Test
    void success_test() throws Exception {

        // given
        RefreshToken refreshToken = RefreshToken.of(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0IjoxNjc1MjEwODc5LCJleHAiOjE2NzY0MjA0NzksInVzZXJJZCI6MX0.Fae1uwS2RPmSad_Uf7pWA8lNqW-MZtm6wP-MDIHwnp8dQpKgaDms3URZBnAG53V8uU-J1Tl0wPFVR6j5wIQS_Q",
                900);

        given(service.refresh(anyString()))
                .willReturn(
                        TokenRefreshDto.from(AccessToken.of("access_token", 900)));

        // when
        ResultActions result = mvc.perform(post("/api/access-token/refresh")
                .header(
                        HttpHeaders.AUTHORIZATION,
                        refreshToken.grantType() + " " + refreshToken.refreshToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.CONTENT_TYPE))
                .andExpect(jsonPath("$.grantType").value(GrantType.BEARER.getType()))
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.accessTokenExpiresIn").isNotEmpty());

        then(service).should().refresh(anyString());

        // API 문서화
        result
                .andDo(document("token-refresh",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("갱신 토큰(Refresh Token)")
                        ),
                        responseFields(
                                fieldWithPath("grantType").type(JsonFieldType.STRING).description(GrantType.BEARER.getType()),
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("접근 토큰"),
                                fieldWithPath("accessTokenExpiresIn").description("접근 토큰 만료 시간 (단위 초)")
                        )
                ));

    }

}