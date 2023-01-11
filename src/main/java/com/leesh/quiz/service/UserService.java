package com.leesh.quiz.service;

import com.leesh.quiz.domain.quiz.Quiz;
import com.leesh.quiz.domain.quiz.QuizRepository;
import com.leesh.quiz.domain.user.User;
import com.leesh.quiz.domain.user.UserRepository;
import com.leesh.quiz.dto.request.CreateQuizRequest;
import com.leesh.quiz.dto.response.CreateQuizResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final QuizRepository quizRepository;

    public CreateQuizResponse createQuiz(CreateQuizRequest request, String username) {

        User user = userRepository.findByNickname(username)
                .orElseThrow(() -> new RuntimeException("Username not found"));

        Quiz quiz = quizRepository.save(
                request.toEntity(user)
        );

        return CreateQuizResponse.of(quiz.getId());
    }
}
