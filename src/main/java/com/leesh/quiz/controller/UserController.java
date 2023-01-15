package com.leesh.quiz.controller;

import com.leesh.quiz.dto.CreateQuizDto;
import com.leesh.quiz.domain.auth.UserInfo;
import com.leesh.quiz.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    private final UserService userService;
    private final MessageSourceAccessor messageSource;

    @PostMapping("/{username}/quiz")
    public ResponseEntity<CreateQuizDto.Response> createQuiz(
            @AuthenticationPrincipal UserInfo loginUserInfo,
            @RequestBody @Valid CreateQuizDto.Request request,
            @PathVariable("username") String username) {

        if (!loginUserInfo.getUsername().equals(username)) {
            throw new IllegalStateException(messageSource.getMessage("username.not.match.login.user"));
        }

        var body = userService.createQuiz(request, username);
        return ResponseEntity.ok(body);
    }

}
