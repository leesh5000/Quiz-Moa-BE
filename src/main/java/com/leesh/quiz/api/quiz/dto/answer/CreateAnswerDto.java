package com.leesh.quiz.api.quiz.dto.answer;

import com.leesh.quiz.domain.answer.Answer;
import com.leesh.quiz.domain.quiz.Quiz;
import com.leesh.quiz.domain.user.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class CreateAnswerDto {

    public record Request(String contents) {

        public Answer toEntity(User user, Quiz quiz) {
            return Answer.of(contents, user, quiz);
        }

    }

    public record Response(Long createAnswerId) {
        public static Response from(Long createAnswerId) {
            return new Response(createAnswerId);
        }
    }

}
