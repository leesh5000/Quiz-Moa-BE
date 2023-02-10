package com.leesh.quiz.api.tokenrefresh.dto;

import com.leesh.quiz.global.jwt.dto.AccessToken;

public record TokenRefreshDto(String grantType, String accessToken,
                              Integer accessTokenExpiresIn) {

    public static TokenRefreshDto from(AccessToken accessToken) {
        return new TokenRefreshDto(
                accessToken.grantType(),
                accessToken.accessToken(),
                accessToken.accessTokenExpiresIn()
        );
    }

}
