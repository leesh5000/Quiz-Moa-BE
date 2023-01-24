package com.leesh.quiz.global.error.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExternalException extends RuntimeException {

    private final HttpStatus httpStatus;

    public ExternalException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
