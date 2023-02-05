package com.leesh.quiz.api.userprofile.dto.quiz;

import java.time.LocalDateTime;


public record MyQuizDto(Long id, String title, String contents,
                        int answerCount, String author, int votes,
                        LocalDateTime createdAt, LocalDateTime modifiedAt) {
}
