package com.leesh.quiz.api.quiz.dto.vote;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class QuizVoteDto {

    public record Request(int value) {

    }

    public record Response(int value) {

    }

}
