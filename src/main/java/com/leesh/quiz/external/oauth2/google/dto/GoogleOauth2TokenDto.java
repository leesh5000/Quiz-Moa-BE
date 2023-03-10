package com.leesh.quiz.external.oauth2.google.dto;

import com.leesh.quiz.external.oauth2.Oauth2Token;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoogleOauth2TokenDto {

    @Getter
    @Builder
    public static class Request {

        private String client_id;
        private String client_secret;
        private String code;
        private String grant_type;
        private String redirect_uri;
        private String code_verifier;

    }

    @Getter
    public static class Response implements Oauth2Token {

        private String token_type;
        private String access_token;
        private Integer expires_in;
        private String refresh_token;
        private String scope;

        private String id_token;

        @Override
        public String getAccessToken() {
            return access_token;
        }
    }

}
