package com.leesh.quiz.web.dto;

import com.leesh.quiz.domain.user.validation.UserEmail;
import com.leesh.quiz.domain.user.validation.UserPassword;

public interface AuthenticateDto {

    record Request(@UserEmail String email, @UserPassword String password) {
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
