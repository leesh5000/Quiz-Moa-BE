package com.leesh.quiz.api.answer.dto;

import com.leesh.quiz.domain.answer.Answer;
import com.leesh.quiz.domain.answervote.AnswerVote;
import com.leesh.quiz.domain.answervote.validation.AnswerVoteValue;
import com.leesh.quiz.domain.user.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateAnswerVoteDto {

    public record Request(@AnswerVoteValue Byte value) {

        public AnswerVote toEntity(User voter, Answer answer) {
            return AnswerVote.of(voter, answer, value);
        }

    }

    public record Response(Long createdVoteId) {
        public static Response from(Long createdVoteId) {
            return new Response(createdVoteId);
        }
    }

}
