package com.leesh.quiz.api.userprofile.dto;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDateTime;

public record MyAnswerDto(Long id, String contents,
                          String author, int votes,
                          LocalDateTime createdAt, LocalDateTime modifiedAt) {

    @QueryProjection
    public MyAnswerDto {
    }
}