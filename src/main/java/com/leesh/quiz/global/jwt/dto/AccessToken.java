package com.leesh.quiz.global.jwt.dto;

import com.leesh.quiz.global.jwt.constant.GrantType;

public record AccessToken(String grantType, String accessToken,
                             Integer accessTokenExpiresIn) {

    public static AccessToken of(String accessToken, Integer accessTokenExpiresIn) {
        return new AccessToken(GrantType.BEARER.getType(), accessToken, accessTokenExpiresIn);
    }

}
