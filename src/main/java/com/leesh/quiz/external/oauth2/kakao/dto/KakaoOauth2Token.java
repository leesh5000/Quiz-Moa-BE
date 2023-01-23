package com.leesh.quiz.external.oauth2.kakao.dto;

import com.leesh.quiz.external.oauth2.Oauth2Token;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoOauth2Token {

    @Getter
    @Builder
    public static class Request {
        private String grant_type;
        private String client_id;
        private String redirect_uri;
        private String code;
        private String client_secret;
        private String state;
    }

    @Getter
    public static class Response implements Oauth2Token {
        private String token_type;
        private String access_token;
        private Integer expires_in;
        private String refresh_token;
        private Integer refresh_token_expires_in;
        private String scope;

        @Override
        public String getAccessToken() {
            return access_token;
        }
    }

}
