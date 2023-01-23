package com.leesh.quiz.external.oauth2.naver.client;

import com.leesh.quiz.external.oauth2.naver.dto.NaverUserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "https://openapi.naver.com", name = "naverApiClient")
public interface NaverApiClient {

        @GetMapping(value = "/v1/nid/me", consumes = "application/json")
        NaverUserInfoResponse getUserInfo(@RequestHeader("Content-type") String contentType,
                                          @RequestHeader("Authorization") String accessToken);

}
