package com.leesh.quiz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leesh.quiz.config.TestSecurityConfiguration;
import com.leesh.quiz.domain.user.Role;
import com.leesh.quiz.dto.request.CreateQuizRequest;
import com.leesh.quiz.dto.response.CreateQuizResponse;
import com.leesh.quiz.security.token.TokenService;
import com.leesh.quiz.security.token.jwt.JwtUserInfo;
import com.leesh.quiz.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("유저 컨트롤러, 인증 필터 단위 테스트")
@Import(TestSecurityConfiguration.class)
@WebMvcTest({UserController.class, TokenService.class})
class UserControllerTest {

    private final MockMvc mvc;

    private final ObjectMapper objectMapper;

    private final String token;

    @MockBean
    private UserService userService;

    public UserControllerTest(@Autowired MockMvc mvc,
                              @Autowired ObjectMapper objectMapper,
                              @Autowired TokenService<String> tokenService
                              ) {

        this.mvc = mvc;
        this.objectMapper = objectMapper;

        // JWT 토큰 생성
        var userInfo = JwtUserInfo.of("userId", "username", Set.of(Role.USER));
        this.token = tokenService.generateToken(userInfo);

    }

    @DisplayName("[createQuiz 성공 테스트] createQuiz 메소드가 요청되면, 반환 값에는 200 상태코드와 quiz Id가 존재해야 한다.")
    @Test
    public void createQuiz_success_test() throws Exception {

        // given 1 : CreateQuizRequest 객체 생성
        var request = CreateQuizRequest.of("title", "content");

        // given 2 : userService.createQuiz() 메소드는 CreateQuizResponse 객체를 반환한다.
        var username = "username";
        var createQuizId = 1L;
        given(userService.createQuiz(request, username))
                .willReturn(CreateQuizResponse.of(createQuizId));

        // when & then 1 : createQuiz 메소드가 요청되면, 반환 값에는 200 상태코드와 quiz Id가 존재해야 한다.
        mvc.perform(
                post("/api/v1/user/{username}/quiz", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .content(
                                objectMapper.writeValueAsString(request)
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.createQuizId").value(createQuizId))
                .andDo(print())
        ;

        // then 2 : userService.createQuiz() 메소드가 호출되어야 한다.
        then(userService).should().createQuiz(request, username);

    }

    @DisplayName("[createQuiz 실패 테스트] URL 경로 상의 {username}과 현재 로그인 한 유저의 username이 다른 경우에는 IllegalStateException 예외가 발생해야 한다.")
    @Test
    public void createQuiz_fail_test_1() throws Exception {

    }

}