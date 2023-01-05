package com.leesh.quiz.security;

import com.leesh.quiz.security.dto.LoginRequest;
import com.leesh.quiz.security.dto.RegisterRequest;
import com.leesh.quiz.security.dto.TokenResponse;
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
    public ResponseEntity<TokenResponse> register(@RequestBody RegisterRequest request) {

        var body = service.register(request);

        return ResponseEntity.ok(body);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody LoginRequest request) {

        var body = service.authenticate(request);

        return ResponseEntity.ok(body);
    }

}
