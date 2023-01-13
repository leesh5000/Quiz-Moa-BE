package com.leesh.quiz.controller;

import com.leesh.quiz.dto.request.CreateQuizRequest;
import com.leesh.quiz.dto.response.CreateQuizResponse;
import com.leesh.quiz.security.token.UserInfo;
import com.leesh.quiz.security.token.jwt.JwtUserInfo;
import com.leesh.quiz.service.UserService;
import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("유저 컨트롤러 단위 테스트")
@ImportAutoConfiguration(MessageSourceAutoConfiguration.class)
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

    @Mock
    private MessageSourceAccessor messageSource;

    @DisplayName("[createQuiz 성공 테스트] createQuiz 메소드가 요청되면, 반환 값에는 200 상태코드와 quiz Id가 존재해야 한다.")
    @Test
    public void createQuiz_success_test() throws JSONException {

        var request = CreateQuizRequest.of("title", "content");
        var username = "username";
        UserInfo userInfo = JwtUserInfo.of("userId", "username", null);

        // given 1 : userService.createQuiz 메소드가 호출되면, CreateQuizResponse 객체를 반환한다.
        long quizId = 1L;
        given(userService.createQuiz(request, username))
                .willReturn(CreateQuizResponse.of(quizId));

        // when : createQuiz 메소드가 요청되면
        var response = sut.createQuiz(userInfo, request, username);

        // then 1 : userSerivce.createQuiz 메소드가 호출된다.
        then(userService).should().createQuiz(request, username);

        // then 2 : 반환 값에는 200 상태코드와 quiz Id가 존재해야 한다.
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assert response.getBody() != null;
        assertThat(response.getBody().createQuizId()).isEqualTo(quizId);

    }

    @DisplayName("[createQuiz 실패 테스트] URL 경로 상의 {username}과 현재 로그인 한 유저의 username이 다른 경우에는 IllegalStateException 예외가 발생해야 한다.")
    @Test
    public void createQuiz_fail_test_1() throws JSONException {

        var request = CreateQuizRequest.of("title", "content");
        var username = "username";
        UserInfo userInfo = JwtUserInfo.of("userId", "username2", null);

        // when 1 : createQuiz 메소드가 요청되면,
        // then 1 : IllegalStateException 예외가 발생해야 한다.
        assertThatThrownBy(
                () -> sut.createQuiz(userInfo, request, username))
                .isInstanceOf(IllegalStateException.class);

        // then 2 : messageSource.getMessage 가 호출되야 한다.
        then(messageSource).should().getMessage(anyString());
    }

}