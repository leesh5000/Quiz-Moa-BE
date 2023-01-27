package com.leesh.quiz.api.logout.controller;

import com.leesh.quiz.api.logout.service.LogoutService;
import com.leesh.quiz.global.constant.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.leesh.quiz.global.util.AuthorizationHeaderUtils.extractAuthorization;

@RequiredArgsConstructor
@RequestMapping("/api")
@ControllerAdvice
public class LogoutController {

    private final LogoutService logoutService;

    @PostMapping(path = "/logout", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> logout(HttpServletRequest request, @AuthenticationPrincipal UserInfo userInfo) {

        logoutService.logout(userInfo);

        return ResponseEntity.ok("logout success");

    }

}
