package com.leesh.quiz.api.userprofile.dto.quiz;

import com.leesh.quiz.domain.quiz.validation.QuizTitle;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EditMyQuizDto {

    @Builder
    public record Request(@QuizTitle String title, String contents) {

    }

    public record Response(Long editQuizId) {

        public static Response from(Long editQuizId) {
            return new Response(editQuizId);
        }
    }

}
