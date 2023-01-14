package com.leesh.quiz.security;

import com.leesh.quiz.security.dto.AuthenticateDto;
import com.leesh.quiz.security.dto.RegisterDto;
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

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<RegisterDto.Response> register(@RequestBody RegisterDto.Request request) {

        var body = service.register(request);

        return ResponseEntity.ok(body);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticateDto.Response> authenticate(@RequestBody AuthenticateDto.Request request) {

        var body = service.authenticate(request);

        return ResponseEntity.ok(body);
    }

}
