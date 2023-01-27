package com.leesh.quiz.global.jwt.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.leesh.quiz.global.jwt.constant.GrantType;

import java.util.Date;

public record AccessToken(String grantType, String accessToken,
                          @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
                             Date accessTokenExpiresIn) {

    public static AccessToken of(String accessToken, Date accessTokenExpiresIn) {
        return new AccessToken(GrantType.BEARER.getType(), accessToken, accessTokenExpiresIn);
    }

}
