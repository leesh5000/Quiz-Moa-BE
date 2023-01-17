package com.leesh.quiz.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

import static com.leesh.quiz.constant.Constants.ERRORS;

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
    @ExceptionHandler({IllegalArgumentException.class, NoSuchElementException.class, RuntimeException.class, IllegalStateException.class})
    protected ResponseEntity<Map<String, Object>> businessExceptionHandler(RuntimeException ex) {

         var body = createResponseBody(ex.getMessage());

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
    protected ResponseEntity<Map<String, Object>> validationFailureExHandler(MethodArgumentNotValidException ex) {

        var errors = new HashMap<String, List<String>>();

        for (var error : ex.getBindingResult().getFieldErrors()) {

            String key = error.getField();
            String message = Optional.ofNullable(
                    error.getDefaultMessage())
                    .orElse("");

            if (errors.containsKey(key)) {
                errors
                    .get(key)
                    .add(message);
            } else {
                errors.put(key, new ArrayList<>(List.of(message)));
            }

        }

        HashMap<String, Object> body = createResponseBody(errors);

        return ResponseEntity.badRequest().body(body);
    }

    private HashMap<String, Object> createResponseBody(Object errors) {
        var body = new HashMap<String, Object>();
        body.put(ERRORS, errors);
        return body;
    }
}
