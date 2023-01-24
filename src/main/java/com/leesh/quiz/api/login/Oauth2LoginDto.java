package com.leesh.quiz.api.login;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.leesh.quiz.global.jwt.TokenDto;
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

        public static Response of(TokenDto tokenDto) {

            return new Response(
                    tokenDto.grantType(),
                    tokenDto.accessToken(),
                    tokenDto.refreshToken(),
                    tokenDto.accessTokenExpireTime(),
                    tokenDto.refreshTokenExpireTime()
            );
        }

    }
}
