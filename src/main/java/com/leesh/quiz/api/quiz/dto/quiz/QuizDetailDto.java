package com.leesh.quiz.api.quiz.dto.quiz;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record QuizDetailDto(Long id, String title, String contents,
                            List<AnswerDto> answers, String author, int votes,
                            LocalDateTime createdAt, LocalDateTime modifiedAt) {

    @QueryProjection
    public QuizDetailDto(Long id, String title, String contents,
                         String author, int votes,
                         LocalDateTime createdAt, LocalDateTime modifiedAt) {

        this(id, title, contents, new ArrayList<>(), author, votes, createdAt, modifiedAt);

    }

    public record AnswerDto(Long id, String contents,
                            String author, int votes,
                            LocalDateTime createdAt, LocalDateTime modifiedAt) {

        @QueryProjection
        public AnswerDto {}

    }
}
