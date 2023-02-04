package com.leesh.quiz.api.userprofile.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EditMyQuizDto {

    @Builder
    public record Request(String title, String contents) {

    }

    public record Response(Long editQuizId) {

        public static Response from(Long editQuizId) {
            return new Response(editQuizId);
        }
    }

}
