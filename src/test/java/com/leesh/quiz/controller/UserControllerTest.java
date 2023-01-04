package com.leesh.quiz.controller;

import com.leesh.quiz.dto.UserJoinDto;
import com.leesh.quiz.service.UserService;
import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
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

    @DisplayName("join 이 요청되면, 유저 서비스의 join 메소드가 실행되고, 200 응답을 반환해야한다.")
    @Test
    public void join_test() throws JSONException {

        // given & when : 유저 컨트롤러의 join 이 요청되면,
        ResponseEntity<Void> response = sut.join(UserJoinDto.of("test1@gmail.com", "test1", "test1"));

        // then : 유저 서비스의 join 메소드가 실행되야 하고, 컨트롤러는 200 응답을 반환해야 한다.
        then(userService).should().join(any(UserJoinDto.class));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

}