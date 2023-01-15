package com.leesh.quiz.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
public class ControllerExceptionHandler {

    /**
     * 비즈니스적인 에러를 처리하는 핸들러
     * <p>
     * EX) 중복 회원가입, 존재하지 않는 아이디, 글 작성 권한이 없음 등 ...
     * </p>
     *
     * @see NoSuchElementException
     * @see IllegalArgumentException
     */
    @ExceptionHandler({IllegalArgumentException.class, NoSuchElementException.class})
    protected ResponseEntity<Map<String, String>> businessExceptionHandler(RuntimeException ex) {

        var body = new HashMap<String, String>();
        body.put("error", ex.getMessage());

        return ResponseEntity.badRequest().body(body);
    }

    /**
     * 파라미터 입력값에 대한 에러를 처리하는 핸들러
     * <p>
     * 어노테이션을 통해 유효성 검사에 실패한 필드들을 담는다.
     * </p>
     *
     * @see MethodArgumentNotValidException
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Map<String, List<String>>> validationFailureExHandler(MethodArgumentNotValidException ex) {

        var body = new HashMap<String, List<String>>();

        for (var error : ex.getBindingResult().getFieldErrors()) {

            String key = error.getField();
            String message = Optional.ofNullable(
                    error.getDefaultMessage())
                    .orElse("");

            if (body.containsKey(key)) {
                List<String> errors = body.get(key);
                errors.add(message);
            } else {
                body.put(key, new ArrayList<>(List.of(message)));
            }

        }

        return ResponseEntity.badRequest().body(body);
    }

}
