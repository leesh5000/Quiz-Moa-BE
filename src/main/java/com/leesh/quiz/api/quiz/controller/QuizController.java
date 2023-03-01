package com.leesh.quiz.api.quiz.controller;

import com.leesh.quiz.api.quiz.dto.answer.CreateAnswerDto;
import com.leesh.quiz.api.quiz.dto.quiz.CreateQuizDto;
import com.leesh.quiz.api.quiz.dto.quiz.QuizDetailDto;
import com.leesh.quiz.api.quiz.dto.quiz.QuizDto;
import com.leesh.quiz.api.quiz.dto.vote.CreateQuizVoteDto;
import com.leesh.quiz.api.quiz.service.QuizService;
import com.leesh.quiz.global.constant.PagingResponseDto;
import com.leesh.quiz.global.constant.UserInfo;
import com.leesh.quiz.global.resolver.LoginUser.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/quizzes")
@RestController
public class QuizController {

    private final QuizService quizService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateQuizDto.Response> createQuiz(@LoginUser UserInfo userInfo,
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
    public ResponseEntity<CreateAnswerDto.Response> createAnswer(@LoginUser UserInfo userInfo,
                                                                 @PathVariable("quiz-id") Long quizId,
                                                                 @RequestBody @Valid CreateAnswerDto.Request request) {

        CreateAnswerDto.Response body = quizService.createAnswer(userInfo, quizId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(body);

    }

    @PostMapping(value = "/{quiz-id}/votes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateQuizVoteDto.Response> createQuizVote(@LoginUser UserInfo userInfo,
                                                                     @PathVariable("quiz-id") Long quizId,
                                                                     @RequestBody @Valid CreateQuizVoteDto.Request request) {

        CreateQuizVoteDto.Response body = quizService.createQuizVote(userInfo, quizId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(body);

    }

}
