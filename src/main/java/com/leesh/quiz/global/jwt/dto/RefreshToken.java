package com.leesh.quiz.global.jwt.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.leesh.quiz.global.jwt.constant.GrantType;

import java.util.Date;

public record RefreshToken(String grantType, String refreshToken,
                           @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
                             Date refreshTokenExpiresIn) {

    public static RefreshToken of(String accessToken, Date accessTokenExpiresIn) {
        return new RefreshToken(GrantType.BEARER.getType(), accessToken, accessTokenExpiresIn);
    }

}
