package com.leesh.quiz.api.quiz.dto.vote;

import com.leesh.quiz.domain.quiz.Quiz;
import com.leesh.quiz.domain.quizvote.QuizVote;
import com.leesh.quiz.domain.quizvote.validation.QuizVoteValue;
import com.leesh.quiz.domain.user.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class CreateQuizVoteDto {

    public record Request(@QuizVoteValue Byte value) {

        public QuizVote toEntity(User voter, Quiz quiz) {
            return QuizVote.of(voter, quiz, value);
        }

    }

    public record Response(long createdVoteId) {

        public static Response from(long createdVoteId) {
            return new Response(createdVoteId);
        }

    }

}
