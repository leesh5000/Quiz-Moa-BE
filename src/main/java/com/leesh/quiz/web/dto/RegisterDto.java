package com.leesh.quiz.web.dto;

import com.leesh.quiz.domain.user.validation.UserEmail;
import com.leesh.quiz.domain.user.validation.UserNickname;
import com.leesh.quiz.domain.user.validation.UserPassword;

public interface RegisterDto {

    record Request(@UserNickname String nickname, @UserEmail String email, @UserPassword String password) {
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
