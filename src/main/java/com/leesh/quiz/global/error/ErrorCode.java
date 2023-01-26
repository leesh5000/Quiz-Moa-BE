package com.leesh.quiz.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
public enum ErrorCode {

    /* Authentication */
    EXPIRED_ACCESS_TOKEN(UNAUTHORIZED, "A-001", "접근 토큰이 만료되었습니다."),
    INVALID_ACCESS_TOKEN(UNAUTHORIZED, "A-002", "접근 토큰이 유효하지 않습니다."),

    INVALID_AUTHORIZATION_HEADER(UNAUTHORIZED, "A-003", "올바르지 않은 Authorization 헤더입니다."),
    NOT_EXIST_AUTHORIZATION(UNAUTHORIZED, "A-004", "Authorization 헤더가 존재하지 않습니다."),
    NOT_BEARER_TYPE_AUTHORIZATION(UNAUTHORIZED, "A-005", "Bearer 타입의 Authorization 헤더가 아닙니다."),
    ACCESS_TOKEN_NOT_FOUND(UNAUTHORIZED, "A-006", "접근 토큰이 존재하지 않습니다."),
    NOT_ACCESS_TOKEN_TYPE(UNAUTHORIZED, "A-007", "접근 토큰이 아닙니다."),
    INVALID_REFRESH_TOKEN(UNAUTHORIZED, "A-008", "유효하지 못한 리프레시 토큰입니다. 다시 로그인 해주세요."),
    REFRESH_TOKEN_EXPIRED(UNAUTHORIZED, "A-009", "리프레시 토큰이 만료되었습니다."),
    NOT_REFRESH_TOKEN_TYPE(UNAUTHORIZED, "A-010", "리프레시 토큰이 아닙니다."),

    /* Oauth2 */
    NOT_SUPPORT_OAUTH2_TYPE(BAD_REQUEST, "O-001", "지원하지 않는 Oauth2 타입입니다."),
    ALREADY_REGISTERED_FROM_KAKAO(BAD_REQUEST, "O-002", "카카오 소셜 계정으로 이미 가입된 이메일 입니다."),
    ALREADY_REGISTERED_FROM_GOOGLE(BAD_REQUEST, "O-003", "구글 소셜 계정으로 이미 가입된 이메일 입니다."),
    ALREADY_REGISTERED_FROM_NAVER(BAD_REQUEST, "O-004", "네이버 소셜 계정으로 이미 가입된 이메일 입니다."),

    /* User */
    DUPLICATED_EMAIL(BAD_REQUEST, "M-001", "이미 존재하는 이메일 입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
    private String parameter;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    ErrorCode(HttpStatus httpStatus, String code, String message, String parameter) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
        this.parameter = parameter;
    }
    }
