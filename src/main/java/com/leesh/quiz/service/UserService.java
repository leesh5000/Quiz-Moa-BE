package com.leesh.quiz.service;

import com.leesh.quiz.domain.quiz.Quiz;
import com.leesh.quiz.domain.quiz.QuizRepository;
import com.leesh.quiz.domain.user.User;
import com.leesh.quiz.domain.user.UserRepository;
import com.leesh.quiz.web.dto.CreateQuizDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final QuizRepository quizRepository;
    private final MessageSourceAccessor messageSource;

    public CreateQuizDto.Response createQuiz(CreateQuizDto.Request request, String username) {

        User user = isExistingUser(username);

        Quiz quiz = request.toEntity(user);

        quizRepository.save(quiz);

        return CreateQuizDto.Response.of(quiz.getId());
    }

    private User isExistingUser(String username) {
        return userRepository.findByNickname(username)
                .orElseThrow(
                        () -> new NoSuchElementException(messageSource.getMessage("user.not.found"))
                );
    }
}
