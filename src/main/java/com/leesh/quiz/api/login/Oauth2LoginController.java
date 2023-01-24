package com.leesh.quiz.api.login;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/oauth2")
@RestController
public class Oauth2LoginController {

    private final Oauth2LoginService oauth2LoginService;

    @PostMapping(path = "/login", consumes = "application/json")
    public ResponseEntity<Oauth2LoginDto.Response> oauth2Login(@RequestBody Oauth2LoginDto.Request request) {

        // TODO authorizationCode, Provider를 유효성 검증할 것

        // oauth2 AccessToken을 가져온다.
        Oauth2LoginDto.Response response = oauth2LoginService.oauth2Login(request);

        return ResponseEntity.ok(response);
    }

}
