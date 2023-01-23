package com.leesh.quiz.api.login;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Oauth2LoginDto {

    public record Request(String oauth2Type, String authorizationCode) {

        public static Request of(String oauth2Type, String authorizationCode) {
            return new Request(oauth2Type, authorizationCode);
        }

    }

}
