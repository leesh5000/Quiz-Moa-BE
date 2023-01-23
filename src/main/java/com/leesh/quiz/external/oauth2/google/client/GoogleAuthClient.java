package com.leesh.quiz.external.oauth2.google.client;

import com.leesh.quiz.external.oauth2.google.dto.GoogleOauth2Token;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "https://oauth2.googleapis.com", name = "googleAuthClient")
public interface GoogleAuthClient {

    @PostMapping(value = "/token", consumes = "application/json")
    GoogleOauth2Token.Response requestToken(@RequestHeader("Content-Type") String contentType,
                                            @SpringQueryMap GoogleOauth2Token.Request request);

}
