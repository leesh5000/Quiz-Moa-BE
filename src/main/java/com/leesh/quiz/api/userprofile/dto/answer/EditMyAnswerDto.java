package com.leesh.quiz.api.userprofile.dto.answer;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EditMyAnswerDto {

    @Builder
    public record Request(String contents) {
    }

    public record Response(Long editAnswerId) {
        public static Response from(Long editAnswerId) {
            return new Response(editAnswerId);
        }
    }

}
