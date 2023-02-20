package com.leesh.quiz.api.quiz.dto.quiz;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor // MyBatis에서 기본 생성자 사용
public class QuizDto {

    private Long id;
    private String title;
    private int answerCount;
    private int totalVotesSum;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Author author;

    @Builder
    private QuizDto(Long id, String title,
                    int answerCount, int totalVotesSum,
                    LocalDateTime createdAt, LocalDateTime modifiedAt,
                    Author author) {
        this.id = id;
        this.title = title;
        this.answerCount = answerCount;
        this.totalVotesSum = totalVotesSum;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.author = author;
    }

    @Getter
    @NoArgsConstructor // MyBatis에서 기본 생성자 사용
    public static class Author {
        private Long id;
        private String username;
        private String email;

        @Builder
        private Author(Long id, String username, String email) {
            this.id = id;
            this.username = username;
            this.email = email;
        }
    }

}