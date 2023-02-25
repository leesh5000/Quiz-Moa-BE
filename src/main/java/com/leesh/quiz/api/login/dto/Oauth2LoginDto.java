package com.leesh.quiz.api.login.dto;

import com.leesh.quiz.global.jwt.constant.GrantType;
import com.leesh.quiz.global.jwt.dto.AccessToken;
import com.leesh.quiz.global.jwt.dto.RefreshToken;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Oauth2LoginDto {

    public record Request(String oauth2Type, String authorizationCode) {

    }

    public record Response(String grantType, String accessToken, String refreshToken,
                           Integer accessTokenExpiresIn, Integer refreshTokenExpiresIn) {

        public static Response from(AccessToken accessToken, RefreshToken refreshToken) {

            return new Response(
                    GrantType.BEARER.getType(),
                    accessToken.accessToken(),
                    refreshToken.refreshToken(),
                    accessToken.accessTokenExpiresIn(),
                    refreshToken.refreshTokenExpiresIn()
            );
        }
    }

}
