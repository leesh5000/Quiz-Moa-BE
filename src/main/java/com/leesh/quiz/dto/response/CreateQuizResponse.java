package com.leesh.quiz.dto.response;

public record CreateQuizResponse(Long createQuizId) {
    public static CreateQuizResponse of(Long createQuizId) {
        return new CreateQuizResponse(createQuizId);
    }
}
