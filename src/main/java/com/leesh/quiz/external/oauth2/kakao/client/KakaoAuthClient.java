package com.leesh.quiz.external.oauth2.kakao.client;

import com.leesh.quiz.external.oauth2.kakao.dto.KakaoOauth2TokenDto;
import com.leesh.quiz.global.logging.Logging;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "https://kauth.kakao.com", name = "kakaoTokenClient")
public interface KakaoAuthClient {

    @PostMapping(value = "/oauth/token", consumes = "application/json")
    @Logging
    KakaoOauth2TokenDto.Response requestToken(@RequestHeader("Content-Type") String contentType,
                                              @SpringQueryMap KakaoOauth2TokenDto.Request request
    );

}

