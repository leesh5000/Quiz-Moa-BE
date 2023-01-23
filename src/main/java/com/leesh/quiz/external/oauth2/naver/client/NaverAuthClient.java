package com.leesh.quiz.external.oauth2.naver.client;

import com.leesh.quiz.external.oauth2.kakao.dto.KakaoOauth2Token;
import com.leesh.quiz.external.oauth2.naver.dto.NaverOauth2Token;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "https://nid.naver.com", name = "naverAuthClient")
public interface NaverAuthClient {

    @PostMapping(value = "/oauth2.0/token", consumes = "application/json")
    NaverOauth2Token.Response requestToken(@RequestHeader("Content-Type") String contentType,
                                           @SpringQueryMap NaverOauth2Token.Request request
    );

}
