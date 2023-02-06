package com.leesh.quiz.api.quiz.controller;

import com.leesh.quiz.api.quiz.dto.answer.CreateAnswerDto;
import com.leesh.quiz.api.quiz.dto.quiz.CreateQuizDto;
import com.leesh.quiz.api.quiz.dto.quiz.QuizDetailDto;
import com.leesh.quiz.api.quiz.dto.quiz.QuizDto;
import com.leesh.quiz.api.quiz.dto.vote.QuizVoteDto;
import com.leesh.quiz.api.quiz.service.QuizService;
import com.leesh.quiz.global.constant.PagingResponseDto;
import com.leesh.quiz.global.constant.UserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagingResponseDto<QuizDto>> getQuizzes(@PageableDefault(size = 20) Pageable pageable) {

        PagingResponseDto<QuizDto> body = quizService.getQuizzesByPaging(pageable);

        return ResponseEntity.ok(body);

    }

    @GetMapping(value = "/{quizId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuizDetailDto> getQuizDetail(@PathVariable Long quizId) {

        QuizDetailDto body = quizService.getQuizDetail(quizId);

        return ResponseEntity.ok(body);

    }

    @PostMapping(value = "/{quiz-id}/answers", consumes = MediaType.APPLICATION_JSON_VALUE ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateAnswerDto.Response> createAnswer(@AuthenticationPrincipal UserInfo userInfo,
                                                                 @PathVariable("quiz-id") Long quizId,
                                                                 @RequestBody @Valid CreateAnswerDto.Request request) {

        CreateAnswerDto.Response body = quizService.createAnswer(userInfo, quizId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(body);

    }

    @PostMapping(value = "/{quiz-id}/votes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuizVoteDto.Response> vote(@AuthenticationPrincipal UserInfo userInfo,
                                                    @PathVariable("quiz-id") Long quizId,
                                                     @RequestBody @Valid QuizVoteDto.Request request) {

        quizService.vote(userInfo, quizId, request);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}
