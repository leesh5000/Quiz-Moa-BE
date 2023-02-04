package com.leesh.quiz.api.userprofile.dto;

public record DeleteMyQuizDto(Long deleteQuizId) {
    public static DeleteMyQuizDto from(Long quizId) {
        return new DeleteMyQuizDto(quizId);
    }
}
