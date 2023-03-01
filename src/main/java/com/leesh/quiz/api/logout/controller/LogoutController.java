package com.leesh.quiz.api.logout.controller;

import com.leesh.quiz.api.logout.service.LogoutService;
import com.leesh.quiz.global.constant.UserInfo;
import com.leesh.quiz.global.resolver.LoginUser.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class LogoutController {

    private final LogoutService logoutService;

    @GetMapping(path = "/logout")
    public ResponseEntity<?> logout(@LoginUser UserInfo userInfo) {

        logoutService.logout(userInfo);

        return ResponseEntity.ok("logout success");

    }

}
