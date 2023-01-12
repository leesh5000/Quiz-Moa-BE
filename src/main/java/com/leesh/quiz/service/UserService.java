package com.leesh.quiz.service;

import com.leesh.quiz.domain.quiz.Quiz;
import com.leesh.quiz.domain.quiz.QuizRepository;
import com.leesh.quiz.domain.user.User;
import com.leesh.quiz.domain.user.UserRepository;
import com.leesh.quiz.dto.request.CreateQuizRequest;
import com.leesh.quiz.dto.response.CreateQuizResponse;
import com.leesh.quiz.security.token.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final QuizRepository quizRepository;

    public CreateQuizResponse createQuiz(CreateQuizRequest request, String username, UserInfo loginUserInfo) {

        if (!loginUserInfo.getUsername().equals(username)) {
            throw new IllegalStateException("퀴즈 생성자와 로그인 사용자가 다릅니다.");
        }

        User user = userRepository.findByNickname(username)
                .orElseThrow(
                        () -> new IllegalStateException("해당 유저가 존재하지 않습니다.")
                );

        Quiz quiz = request.toEntity(user);

        quizRepository.save(quiz);

        return CreateQuizResponse.of(quiz.getId());
    }
}
