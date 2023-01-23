package com.leesh.quiz.external.oauth2.kakao.client;

import com.leesh.quiz.external.oauth2.kakao.dto.KakaoOauth2Token;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "https://kauth.kakao.com", name = "kakaoTokenClient")
public interface KakaoAuthClient {

    @PostMapping(value = "/oauth/token", consumes = "application/json")
    KakaoOauth2Token.Response requestToken(@RequestHeader("Content-Type") String contentType,
                                           @SpringQueryMap KakaoOauth2Token.Request request
    );

}

