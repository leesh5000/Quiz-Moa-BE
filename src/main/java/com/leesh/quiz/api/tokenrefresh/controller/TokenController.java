package com.leesh.quiz.api.tokenrefresh.controller;

import com.leesh.quiz.api.tokenrefresh.dto.TokenRefreshDto;
import com.leesh.quiz.api.tokenrefresh.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.leesh.quiz.global.util.AuthorizationHeaderUtils.extractToken;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class TokenController {

    private final TokenService tokenService;

    @PostMapping(path = "/access-token/issue")
    public ResponseEntity<TokenRefreshDto> refresh(HttpServletRequest request) {

        String refreshToken = extractToken(request);

        TokenRefreshDto body = tokenService.refresh(refreshToken);

        return ResponseEntity.ok(body);
    }


}
