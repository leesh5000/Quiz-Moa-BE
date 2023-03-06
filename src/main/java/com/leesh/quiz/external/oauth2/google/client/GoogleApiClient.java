package com.leesh.quiz.external.oauth2.google.client;

import com.leesh.quiz.external.oauth2.google.dto.GoogleUserInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "https://www.googleapis.com/oauth2/v3", name = "googleApiClient")
public interface GoogleApiClient {

    @PostMapping(value = "/userinfo?access_token={accessToken}", consumes = "application/json")
    GoogleUserInfoDto getUserInfo(@RequestHeader("Content-type") String contentType,
                                  @PathVariable("accessToken") String accessToken);

}
