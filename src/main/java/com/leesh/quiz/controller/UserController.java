package com.leesh.quiz.controller;

import com.leesh.quiz.dto.request.CreateQuizRequest;
import com.leesh.quiz.dto.response.CreateQuizResponse;
import com.leesh.quiz.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/{username}/quiz")
    public ResponseEntity<CreateQuizResponse> createQuiz(@RequestBody CreateQuizRequest request, @PathVariable("username") String username) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object details = authentication.getDetails();

        var body = userService.createQuiz(request, username);
        return ResponseEntity.ok(body);
    }

}
