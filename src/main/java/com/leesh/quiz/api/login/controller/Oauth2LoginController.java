package com.leesh.quiz.api.login.controller;

import com.leesh.quiz.api.login.dto.Oauth2LoginDto;
import com.leesh.quiz.api.login.service.Oauth2LoginService;
import com.leesh.quiz.domain.user.constant.Oauth2Type;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Oauth2LoginDto.Response> oauth2Login(@RequestBody Oauth2LoginDto.Request request, HttpServletResponse response) {

        // 입력값 유효성 검증
        Oauth2Type.from(request.oauth2Type());

        // oauth2 AccessToken을 가져온다.
        Oauth2LoginDto.Response body = oauth2LoginService.oauth2Login(request);

        return ResponseEntity.ok(body);
    }

}
