package com.leesh.quiz.controller;

import com.leesh.quiz.domain.user.service.UserService;
import com.leesh.quiz.dto.CreateQuizDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    private final UserService userService;
    private final MessageSourceAccessor messageSource;

    @PostMapping("/{username}/quiz")
    public ResponseEntity<CreateQuizDto.Response> createQuiz(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid CreateQuizDto.Request request,
            @PathVariable("username") String username) {

        if (!userDetails.getUsername().equals(username)) {
            throw new IllegalStateException(messageSource.getMessage("username.not.match.login.user"));
        }

        var body = userService.createQuiz(request, username);
        return ResponseEntity.ok(body);
    }

}
