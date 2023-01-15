package com.leesh.quiz.web.dto;

import com.leesh.quiz.domain.quiz.Quiz;
import com.leesh.quiz.domain.quiz.validation.QuizTitle;
import com.leesh.quiz.domain.user.User;

public interface CreateQuizDto {

    record Request(@QuizTitle String title, String content) {
        public static Request of(String title, String content) {
            return new Request(title, content);
        }

        public Quiz toEntity(User user) {
            return Quiz.of(title, content, user);
        }
    }

    record Response(Long createQuizId) {
        public static Response of(Long createQuizId) {
            return new Response(createQuizId);
        }
    }

}
