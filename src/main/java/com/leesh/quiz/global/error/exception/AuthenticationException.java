package com.leesh.quiz.global.error.exception;

import com.leesh.quiz.global.error.ErrorCode;

public class AuthenticationException extends BusinessException {
    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
