package com.leesh.quiz.external.oauth2.kakao.client;

import com.leesh.quiz.external.oauth2.kakao.dto.KakaoUserInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "https://kapi.kakao.com", name = "kakaoApiClient")
public interface KakaoApiClient {

    @GetMapping(value = "/v2/user/me", consumes = "application/json")
    KakaoUserInfoDto getUserInfo(@RequestHeader("Content-type") String contentType,
                                 @RequestHeader("Authorization") String accessToken);

}
