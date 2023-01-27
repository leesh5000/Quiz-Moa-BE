package com.leesh.quiz.api.tokenrefresh.controller;

import com.leesh.quiz.api.tokenrefresh.dto.TokenRefreshDto;
import com.leesh.quiz.api.tokenrefresh.service.TokenRefreshService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.leesh.quiz.global.util.AuthorizationHeaderUtils.extractAuthorization;

@RequiredArgsConstructor
@RequestMapping("/api/access-token")
@RestController
public class TokenRefreshController {

    private final TokenRefreshService tokenRefreshService;

    @PostMapping(path = "/refresh")
    public ResponseEntity<TokenRefreshDto> refresh(HttpServletRequest request) {

        String refreshToken = extractAuthorization(request);

        TokenRefreshDto body = tokenRefreshService.refresh(refreshToken);

        return ResponseEntity.ok(body);
    }


}
