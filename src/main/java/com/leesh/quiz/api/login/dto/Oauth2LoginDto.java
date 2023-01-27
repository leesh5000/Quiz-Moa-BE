package com.leesh.quiz.api.login.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.leesh.quiz.global.jwt.dto.AccessToken;
import com.leesh.quiz.global.jwt.dto.RefreshToken;
import com.leesh.quiz.global.jwt.constant.GrantType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Oauth2LoginDto {

    public record Request(String oauth2Type, String authorizationCode) {

    }

    public record Response(String grantType, String accessToken, String refreshToken,
                           @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
                           Date accessTokenExpireTime,
                           @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
                           Date refreshTokenExpireTime) {

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
