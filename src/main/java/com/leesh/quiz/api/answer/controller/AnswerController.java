package com.leesh.quiz.api.answer.controller;

import com.leesh.quiz.api.answer.dto.CreateAnswerVoteDto;
import com.leesh.quiz.api.answer.service.AnswerService;
import com.leesh.quiz.global.constant.UserInfo;
import com.leesh.quiz.global.resolver.LoginUser.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/answers")
@RestController
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping(value = "{answer-id}/votes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateAnswerVoteDto.Response> createAnswerVote(@LoginUser UserInfo userInfo,
                                                                         @PathVariable("answer-id") Long answerId,
                                                                         @RequestBody @Valid CreateAnswerVoteDto.Request request, @PathVariable("answer-id") String parameter) {

        var body = answerService.createAnswerVote(userInfo, answerId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(body);

    }

}
