package com.leesh.quiz.api.quiz.controller;

import com.leesh.quiz.api.quiz.dto.quiz.CreateQuizDto;
import com.leesh.quiz.api.quiz.service.QuizService;
import com.leesh.quiz.global.constant.UserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/quizzes")
@RestController
public class QuizController {

    private final QuizService quizService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateQuizDto.Response> createQuiz(@AuthenticationPrincipal UserInfo userInfo,
                                        @RequestBody @Valid CreateQuizDto.Request request) {

        CreateQuizDto.Response body = quizService.createQuiz(userInfo, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(body);

    }

}
