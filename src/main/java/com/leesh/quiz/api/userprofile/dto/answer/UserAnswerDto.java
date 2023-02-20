package com.leesh.quiz.api.userprofile.dto.answer;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDateTime;

public record UserAnswerDto(Long id, String contents, Long quizId,
                            AuthorDto author,
                            int totalVotesSum,
                            LocalDateTime createdAt, LocalDateTime modifiedAt) {

    @QueryProjection
    public UserAnswerDto {}

    public record AuthorDto(Long id, String username, String email) {

        @QueryProjection
        public AuthorDto {}

    }
}