package com.leesh.quiz.external.oauth2.naver.dto;

import com.leesh.quiz.external.oauth2.Oauth2Token;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NaverOauth2Token {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String grant_type;
        private String client_id;
        private String client_secret;
        private String code;
        private String state;

    }

    @ToString
    @Builder @Getter
    @NoArgsConstructor @AllArgsConstructor
    public static class Response implements Oauth2Token {
        private String token_type;
        private String access_token;
        private Integer expires_in;
        private String refresh_token;
        private String error;
        private String error_description;

        @Override
        public String getAccessToken() {
            return access_token;
        }
    }

}
