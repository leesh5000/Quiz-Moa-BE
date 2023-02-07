package com.leesh.quiz.api.quiz.dto.quiz;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record QuizDetailDto(Long id, String title, String contents,
                            Long authorId, String author,
                            List<AnswerDto> answers, List<QuizVoteDto> votes,
                            LocalDateTime createdAt, LocalDateTime modifiedAt) {

    @QueryProjection
    public QuizDetailDto(Long id, String title, String contents,
                         Long authorId, String author,
                         List<QuizVoteDto> votes,
                         LocalDateTime createdAt, LocalDateTime modifiedAt) {

        this(id, title, contents, authorId, author, new ArrayList<>(), votes, createdAt, modifiedAt);
    }

    public record AnswerDto(Long id, String contents,
                            Long authorId, String author,
                            List<AnswerVoteDto> votes,
                            LocalDateTime createdAt, LocalDateTime modifiedAt) {

        @QueryProjection
        public AnswerDto {}

    }

    public record QuizVoteDto(Long id, int value,
                              Long voterId, String voter) {

        @QueryProjection
        public QuizVoteDto {}

    }

    public record AnswerVoteDto(Long id, int value,
                                Long voterId, String voter) {

        @QueryProjection
        public AnswerVoteDto {}

    }
}
