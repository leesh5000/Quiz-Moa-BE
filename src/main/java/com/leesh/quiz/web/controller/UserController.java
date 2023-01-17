package com.leesh.quiz.web.controller;

import com.leesh.quiz.service.UserService;
import com.leesh.quiz.web.dto.CreateQuizDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    private final UserService userService;
    private final MessageSourceAccessor messageSource;

    @PostMapping(value = "/{username}/quiz", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateQuizDto.Response> createQuiz(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid CreateQuizDto.Request request,
            @PathVariable("username") String username) {

        validateUsername(userDetails, username);

        var body = userService.createQuiz(request, username);
        return ResponseEntity.ok(body);
    }

    private void validateUsername(UserDetails userDetails, String username) {
        if (!userDetails.getUsername().equals(username)) {
            throw new IllegalArgumentException(
                    messageSource.getMessage("username.not.match.login.user")
            );
        }
    }

}
