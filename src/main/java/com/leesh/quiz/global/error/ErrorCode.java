package com.leesh.quiz.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "A-001", "토큰이 만료되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A-002", "토큰이 유효하지 않습니다."),

    NOT_EXISTS_AUTHORIZATION(HttpStatus.UNAUTHORIZED, "A-003", "Authorization 헤더가 존재하지 않습니다."),
    NOT_VALID_BEARER_GRANT_TYPE(HttpStatus.UNAUTHORIZED, "A-004", "Bearer GrantType이 유효하지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "A-005", "리프레시 토큰이 존재하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A-006", "리프레시 토큰이 만료되었습니다."),
    NOT_SUPPORT_OAUTH2_TYPE(HttpStatus.BAD_REQUEST, "O-001", "지원하지 않는 Oauth2 타입입니다."),
    INVALID_USER_TYPE(HttpStatus.BAD_REQUEST, "M-001", "잘못된 회원 타입 입니다.(memberType : KAKAO)"),
    DUPLICATED_USER(HttpStatus.BAD_REQUEST, "M-002", "이미 가입된 회원입니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
