package com.leesh.quiz.api.tokenrefresh.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.leesh.quiz.global.jwt.dto.AccessToken;
import lombok.Builder;

import java.util.Date;

public record TokenRefreshDto(String grantType, String accessToken,
                              @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
                              Date accessTokenExpiresIn) {

    public static TokenRefreshDto from(AccessToken accessToken) {
        return new TokenRefreshDto(
                accessToken.grantType(),
                accessToken.accessToken(),
                accessToken.accessTokenExpiresIn()
        );
    }

}
