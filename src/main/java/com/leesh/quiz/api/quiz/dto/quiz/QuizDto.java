package com.leesh.quiz.api.quiz.dto.quiz;

import java.time.LocalDateTime;

public record QuizDto(Long id, String title,
                      int answerCount, String author, int votes,
                      LocalDateTime createdAt, LocalDateTime modifiedAt) {

}
