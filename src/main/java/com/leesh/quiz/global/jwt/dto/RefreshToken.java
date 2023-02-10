package com.leesh.quiz.global.jwt.dto;

import com.leesh.quiz.global.jwt.constant.GrantType;

public record RefreshToken(String grantType, String refreshToken, Integer refreshTokenExpiresIn) {

    public static RefreshToken of(String refreshToken, Integer refreshTokenExpiresIn) {
        return new RefreshToken(GrantType.BEARER.getType(), refreshToken, refreshTokenExpiresIn);
    }

}
