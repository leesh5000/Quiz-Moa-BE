package com.leesh.quiz.dto;

public interface RegisterDto {

    record Request(String nickname, String email, String password) {
        public static Request of(String nickname, String email, String password) {
            return new Request(nickname, email, password);
        }
    }

    record Response(String token) {
        public static Response of(String token) {
            return new Response(token);
        }
    }

}
