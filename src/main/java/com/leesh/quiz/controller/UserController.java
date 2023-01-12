package com.leesh.quiz.controller;

import com.leesh.quiz.dto.request.CreateQuizRequest;
import com.leesh.quiz.dto.response.CreateQuizResponse;
import com.leesh.quiz.security.token.UserInfo;
import com.leesh.quiz.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/{username}/quiz")
    public ResponseEntity<CreateQuizResponse> createQuiz(
            @AuthenticationPrincipal UserInfo loginUserInfo,
            @RequestBody CreateQuizRequest request,
            @PathVariable("username") String username) {

        var body = userService.createQuiz(request, username, loginUserInfo);
        return ResponseEntity.ok(body);
    }

}
