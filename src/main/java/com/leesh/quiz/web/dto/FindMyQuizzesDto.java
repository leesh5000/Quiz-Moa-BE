package com.leesh.quiz.web.dto;

import com.leesh.quiz.domain.quiz.Quiz;

public interface FindMyQuizzesDto {

    record Response() {
        public static Response of() {
            return new Response();
        }

        public static Response from(Quiz entity) {
            return new Response(

            );
        }

    }

}
