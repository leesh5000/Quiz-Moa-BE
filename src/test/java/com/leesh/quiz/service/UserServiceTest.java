package com.leesh.quiz.service;

import com.leesh.quiz.domain.quiz.Quiz;
import com.leesh.quiz.domain.quiz.QuizRepository;
import com.leesh.quiz.domain.user.User;
import com.leesh.quiz.domain.user.UserRepository;
import com.leesh.quiz.domain.user.service.UserService;
import com.leesh.quiz.dto.CreateQuizDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("유저 서비스 단위 테스트")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    /**
     * System Under Test - 현재 테스트 중인 대상 <br>
     * {@See <a href="https://en.wikipedia.org/wiki/System_under_test">SUT</a>}
     */
    @InjectMocks
    private UserService sut;

    @Mock
    private UserRepository userRepository;

    @Mock
    private QuizRepository quizRepository;

    @DisplayName("[createQuiz 성공 테스트] createQuiz 메소드가 요청되면, QuizId를 갖는 CreateQuizResponse 가 반환되어야 한다.")
    @Test
    void createQuiz_success_test() {

        var request = CreateQuizDto.Request.of("title", "content");
        String username = "username";

        // given 1 : userRepository.findByNickname 호출되면, User 객체를 반환한다.
        User user = User.of("nickname", "email", "password");
        given(userRepository.findByNickname(username))
                .willReturn(Optional.of(user));

        // given 2 : quizRepository.save 호출되면, Quiz 객체를 반환한다.
        Quiz quiz = Quiz.of("title", "content", user);
        given(quizRepository.save(any(Quiz.class)))
                .willReturn(quiz);

        // when : createQuiz 메소드가 호출되면,
        var response = sut.createQuiz(request, username);

        // then 1 : userRepository.findByNickname이 호출되어야 한다.
        then(userRepository).should().findByNickname(username);

        // then 2 : quizRepository.save가 호출되어야 한다.
        then(quizRepository).should().save(any(Quiz.class));

        // then 3 : CreateQuizResponse 객체가 반환되어야 한다.
        assertThat(response).isNotNull();

    }
}