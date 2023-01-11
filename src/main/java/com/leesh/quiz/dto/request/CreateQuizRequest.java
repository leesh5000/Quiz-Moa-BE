package com.leesh.quiz.dto.request;

import com.leesh.quiz.domain.quiz.Quiz;
import com.leesh.quiz.domain.user.User;

public record CreateQuizRequest(String title, String content) {

    public static CreateQuizRequest of(String title, String content) {
        return new CreateQuizRequest(title, content);
    }

    public Quiz toEntity(User user) {
        return Quiz.of(title, content, user);
    }
}
