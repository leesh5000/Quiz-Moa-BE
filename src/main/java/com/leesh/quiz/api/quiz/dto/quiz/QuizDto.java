package com.leesh.quiz.api.quiz.dto.quiz;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class QuizDto {

    private Long id;
    private String title;
    private int answerCount;
    private int totalVotes;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Author author;

    protected QuizDto() {
    }

    @Builder
    private QuizDto(Long id, String title,
                    int answerCount, int totalVotes,
                    LocalDateTime createdAt, LocalDateTime modifiedAt,
                    Author author) {
        this.id = id;
        this.title = title;
        this.answerCount = answerCount;
        this.totalVotes = totalVotes;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.author = author;
    }

    @Getter
    public static class Author {
        private Long id;
        private String username;
        private String email;

        protected Author() {
        }

        @Builder
        private Author(Long id, String username, String email) {
            this.id = id;
            this.username = username;
            this.email = email;
        }
    }

}