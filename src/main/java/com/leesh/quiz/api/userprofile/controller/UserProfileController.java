package com.leesh.quiz.api.userprofile.controller;

import com.leesh.quiz.api.userprofile.dto.answer.EditMyAnswerDto;
import com.leesh.quiz.api.userprofile.dto.answer.MyAnswerDto;
import com.leesh.quiz.api.userprofile.dto.PagingResponseDto;
import com.leesh.quiz.api.userprofile.dto.quiz.EditMyQuizDto;
import com.leesh.quiz.api.userprofile.dto.quiz.MyQuizDto;
import com.leesh.quiz.api.userprofile.service.UserProfileService;
import com.leesh.quiz.global.constant.UserInfo;
import com.leesh.quiz.global.validator.UserInfoValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users/{user-id}")
public class UserProfileController {

    private final UserProfileService userProfileService;

    // Paging은 Spring Data Jpa에서 제공하는 Pageable 스펙을 사용
    // 다음과 같은 형식 /quizzes?page={page}&size={size}&sort={property,direction}
    @GetMapping(value = "/quizzes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagingResponseDto<MyQuizDto>> getMyQuizzes(@PathVariable("user-id") Long userId,
                                                                     @AuthenticationPrincipal UserInfo userInfo,
                                                                     @PageableDefault(size = 20) Pageable pageable) {

        // 접근 권한이 있는 사용자인지 검증
        UserInfoValidator.validateAccessible(userId, userInfo);

        PagingResponseDto<MyQuizDto> body = userProfileService.getMyQuizzes(pageable, userInfo);

        return ResponseEntity.ok(body);

    }

    @PutMapping(value = "/quizzes/{quiz-id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EditMyQuizDto.Response> editMyQuiz(@PathVariable("user-id") Long userId,
                                                             @PathVariable("quiz-id") Long quizId,
                                                             @AuthenticationPrincipal UserInfo userInfo,
                                                             @RequestBody @Valid EditMyQuizDto.Request request) {

        // 접근 권한이 있는 사용자인지 검증
        UserInfoValidator.validateAccessible(userId, userInfo);

        EditMyQuizDto.Response body = userProfileService.editMyQuiz(request, userInfo, quizId);

        return ResponseEntity.ok(body);

    }

    @DeleteMapping(value = "/quizzes/{quiz-id}")
    public ResponseEntity<Void> deleteMyQuiz(@PathVariable("user-id") Long userId,
                                                        @PathVariable("quiz-id") Long quizId,
                                                        @AuthenticationPrincipal UserInfo userInfo) {

        // 접근 권한이 있는 사용자인지 검증
        UserInfoValidator.validateAccessible(userId, userInfo);

        userProfileService.deleteMyQuiz(quizId, userInfo);

        return ResponseEntity.noContent().build();

    }

    @GetMapping(value = "/answers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagingResponseDto<MyAnswerDto>> getMyAnswers(@PathVariable("user-id") Long userId,
                                                                       @AuthenticationPrincipal UserInfo userInfo,
                                                                       @PageableDefault(size = 20) Pageable pageable) {

        // 접근 권한이 있는 사용자인지 검증
        UserInfoValidator.validateAccessible(userId, userInfo);

        PagingResponseDto<MyAnswerDto> body = userProfileService.getMyAnswers(pageable, userInfo);

        return ResponseEntity.ok(body);

    }

    @PutMapping(value = "/answers/{answer-id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EditMyAnswerDto.Response> editMyAnswer(@PathVariable("user-id") Long userId,
                                                                @PathVariable("answer-id") Long answerId,
                                                                @AuthenticationPrincipal UserInfo userInfo,
                                                                @RequestBody @Valid EditMyAnswerDto.Request request) {

        // 접근 권한이 있는 사용자인지 검증
        UserInfoValidator.validateAccessible(userId, userInfo);

        EditMyAnswerDto.Response body = userProfileService.editMyAnswer(request, userInfo, answerId);

        return ResponseEntity.ok(body);

    }

    @DeleteMapping(value = "/answers/{answer-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteMyAnswer(@PathVariable("user-id") Long userId,
                                                           @PathVariable("answer-id") Long answerId,
                                                           @AuthenticationPrincipal UserInfo userInfo) {

        // 접근 권한이 있는 사용자인지 검증
        UserInfoValidator.validateAccessible(userId, userInfo);

        userProfileService.deleteMyAnswer(answerId, userInfo);

        return ResponseEntity.noContent().build();

    }

}
