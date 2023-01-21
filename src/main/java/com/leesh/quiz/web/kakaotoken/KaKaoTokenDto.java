package com.leesh.quiz.web.kakaotoken;

import lombok.Getter;
import lombok.ToString;

public interface KaKaoTokenDto {

    @Getter
    class Request {
        private String grant_type;
        private String client_id;
        private String redirect_uri;
        private String code;
        private String client_secret;

        protected Request() {
        }

        public Request(String grant_type, String client_id, String redirect_uri, String code, String client_secret) {
            this.grant_type = grant_type;
            this.client_id = client_id;
            this.redirect_uri = redirect_uri;
            this.code = code;
            this.client_secret = client_secret;
        }

        public static Request of(String grant_type, String client_id, String redirect_uri, String code, String client_secret) {
            return new Request(grant_type, client_id, redirect_uri, code, client_secret);
        }

    }

    @ToString
    @Getter
    class Response {
        private String token_type;
        private String access_token;
        private Integer expires_in;
        private String refresh_token;
        private Integer refresh_token_expires_in;
        private String scope;

        protected Response() {
        }

        public Response(String token_type, String access_token, Integer expires_in, String refresh_token, Integer refresh_token_expires_in, String scope) {
            this.token_type = token_type;
            this.access_token = access_token;
            this.expires_in = expires_in;
            this.refresh_token = refresh_token;
            this.refresh_token_expires_in = refresh_token_expires_in;
            this.scope = scope;
        }

        public static Response of(String token_type, String access_token, Integer expires_in, String refresh_token, Integer refresh_token_expires_in, String scope) {
            return new Response(token_type, access_token, expires_in, refresh_token, refresh_token_expires_in, scope);
        }

    }

}
