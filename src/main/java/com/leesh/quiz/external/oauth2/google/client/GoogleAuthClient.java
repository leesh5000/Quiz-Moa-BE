package com.leesh.quiz.external.oauth2.google.client;

import com.leesh.quiz.external.oauth2.google.dto.GoogleOauth2TokenDto;
import com.leesh.quiz.global.logging.Logging;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "https://oauth2.googleapis.com", name = "googleAuthClient")
public interface GoogleAuthClient {

    @PostMapping(value = "/token", consumes = "application/json")
    @Logging
    GoogleOauth2TokenDto.Response requestToken(@RequestHeader("Content-Type") String contentType,
                                               @SpringQueryMap GoogleOauth2TokenDto.Request request);

}
