package com.leesh.quiz.controller;

import com.leesh.quiz.dto.request.CreateQuizRequest;
import com.leesh.quiz.dto.response.CreateQuizResponse;
import com.leesh.quiz.security.token.UserInfo;
import com.leesh.quiz.security.token.jwt.JwtUserInfo;
import com.leesh.quiz.service.UserService;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;

@DisplayName("유저 컨트롤러 단위 테스트")
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    /**
     * System Under Test - 현재 테스트 중인 대상 <br>
     * {@See <a href="https://en.wikipedia.org/wiki/System_under_test">SUT</a>}
     */
    @InjectMocks
    private UserController sut;

    @Mock
    private UserService userService;

    @DisplayName("createQuiz 메소드가 요청되면, 상태코드 200이 반환되고, userService.createQuiz가 호출된다.")
    @Test
    public void create_quiz_test() throws JSONException {

        // given :
        CreateQuizRequest request = CreateQuizRequest.of("title", "content");
        String username = "username";
        UserInfo userInfo = JwtUserInfo.of("userId", "username", null);

        // when : createQuiz 메소드가 요청되면,
        ResponseEntity<CreateQuizResponse> response = sut.createQuiz(userInfo, request, username);

        // then : 상태코드 200이 반환되고, userService.createQuiz가 호출된다.
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(userService).should().createQuiz(any(CreateQuizRequest.class), any(String.class), any(UserInfo.class));

    }

}