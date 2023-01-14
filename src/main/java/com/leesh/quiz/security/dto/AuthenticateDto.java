package com.leesh.quiz.security.dto;

public interface AuthenticateDto {

    record Request(String email, String password) {
        public static Request of(String email, String password) {
            return new Request(email, password);
        }
    }

    record Response(String token) {
        public static Response of(String token) {
            return new Response(token);
        }
    }

}
