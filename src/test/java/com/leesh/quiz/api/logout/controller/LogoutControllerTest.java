package com.leesh.quiz.api.logout.controller;

import com.leesh.quiz.api.logout.service.LogoutService;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("로그아웃 API 테스트")
@WebMvcTest(LogoutController.class)
@Import(MvcTestConfiguration.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs
class LogoutControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LogoutService logoutService;

    @MockBean
    private TokenService tokenService;

    @DisplayName("정상 호출")
    @Test
    public void success() throws Exception {

        // given
        AccessToken accessToken = AccessToken.of(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0IjoxNjc1MjEwODc5LCJleHAiOjE2NzY0MjA0NzksInVzZXJJZCI6MX0.Fae1uwS2RPmSad_Uf7pWA8lNqW-MZtm6wP-MDIHwnp8dQpKgaDms3URZBnAG53V8uU-J1Tl0wPFVR6j5wIQS_Q",
                900);

        Long userId = 1L;
        UserInfo user = UserInfo.builder()
                .userId(userId)
                .email("test1@gmail.com")
                .username("test1")
                .role(Role.USER)
                .build();

        given(tokenService.extractUserInfo(any(String.class)))
                .willReturn(user);

        willDoNothing().given(logoutService)
                .logout(any(UserInfo.class));

        // when
        ResultActions result = mvc.perform(get("/api/logout")
                .header(
                        HttpHeaders.AUTHORIZATION,
                        accessToken.grantType() + " " + accessToken.accessToken())
                .accept(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(status().isOk());

        then(logoutService).should().logout(any(UserInfo.class));

        // API 문서화
        result
                .andDo(document("logout",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("접근 토큰(Access Token)")
                        )
                ));

    }

}