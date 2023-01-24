package com.leesh.quiz.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    /* Token */
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "A-001", "토큰이 만료되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A-002", "토큰이 유효하지 않습니다."),

    NOT_EXISTS_AUTHORIZATION(HttpStatus.UNAUTHORIZED, "A-003", "Authorization 헤더가 존재하지 않습니다."),
    NOT_VALID_BEARER_GRANT_TYPE(HttpStatus.UNAUTHORIZED, "A-004", "Bearer GrantType이 유효하지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "A-005", "리프레시 토큰이 존재하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A-006", "리프레시 토큰이 만료되었습니다."),

    /* Oauth2 */
    NOT_SUPPORT_OAUTH2_TYPE(HttpStatus.BAD_REQUEST, "O-001", "지원하지 않는 Oauth2 타입입니다."),
    ALREADY_REGISTERED_FROM_KAKAO(HttpStatus.BAD_REQUEST, "O-002", "카카오 소셜 계정으로 이미 가입된 이메일 입니다."),
    ALREADY_REGISTERED_FROM_GOOGLE(HttpStatus.BAD_REQUEST, "O-003", "구글 소셜 계정으로 이미 가입된 이메일 입니다."),
    ALREADY_REGISTERED_FROM_NAVER(HttpStatus.BAD_REQUEST, "O-004", "네이버 소셜 계정으로 이미 가입된 이메일 입니다."),

    /* User */
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "M-001", "이미 존재하는 이메일 입니다."),
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
