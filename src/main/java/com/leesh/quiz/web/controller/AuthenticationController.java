package com.leesh.quiz.web.controller;

import com.leesh.quiz.service.AuthenticationService;
import com.leesh.quiz.web.dto.AuthenticateDto;
import com.leesh.quiz.web.dto.RegisterDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticateService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterDto.Request request) {

        authenticateService.register(request);

        return ResponseEntity.ok().build();

    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticateDto.Response> authenticate(@RequestBody @Valid AuthenticateDto.Request request) {

        var body = authenticateService.authenticate(request);

        return ResponseEntity.ok(body);
    }

}
