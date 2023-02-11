package com.leesh.quiz.api.quiz.dto.quiz;

import com.leesh.quiz.domain.quiz.Quiz;
import com.leesh.quiz.domain.quiz.validation.QuizContents;
import com.leesh.quiz.domain.quiz.validation.QuizTitle;
import com.leesh.quiz.domain.user.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class CreateQuizDto {

    public record Request(@QuizTitle String title, @QuizContents String contents) {

        public Quiz toEntity(User user) {
            return Quiz.of(title, contents, user);
        }

    }

    public record Response(Long createQuizId) {
        public static Response from(Long createQuizId) {
            return new Response(createQuizId);
        }
    }

}
