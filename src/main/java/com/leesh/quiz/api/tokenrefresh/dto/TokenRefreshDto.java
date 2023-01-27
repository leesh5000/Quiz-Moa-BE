package com.leesh.quiz.api.tokenrefresh.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.util.Date;

@Builder
public record TokenRefreshDto(String grantType, String accessToken,
                              @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
                              Date accessTokenExpiresIn) {

}
