package com.leesh.quiz.api.quiz.dto.quiz;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record QuizDetailDto(Long id, String title, String contents,
                            AuthorDto author,
                            List<AnswerDto> answers, List<QuizVoteDto> votes,
                            LocalDateTime createdAt, LocalDateTime modifiedAt) {

    @QueryProjection
    public QuizDetailDto(Long id, String title, String contents,
                         AuthorDto authorDto,
                         List<QuizVoteDto> votes,
                         LocalDateTime createdAt, LocalDateTime modifiedAt) {

        this(id, title, contents, authorDto, new ArrayList<>(), votes, createdAt, modifiedAt);
    }

    public record AuthorDto(Long id, String username, String email) {

        @QueryProjection
        public AuthorDto {}

    }

    public record AnswerDto(Long id, String contents,
                            AuthorDto author,
                            List<AnswerVoteDto> votes,
                            LocalDateTime createdAt, LocalDateTime modifiedAt) {

        @QueryProjection
        public AnswerDto {}

    }

    public record QuizVoteDto(Long id, int value, AuthorDto voter) {

        @QueryProjection
        public QuizVoteDto {}

    }

    public record AnswerVoteDto(Long id, int value,
                                AuthorDto voter) {

        @QueryProjection
        public AnswerVoteDto {}

    }
}
