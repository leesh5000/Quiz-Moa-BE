package com.leesh.quiz.global.error;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCode {

    /* Authentication */
    EXPIRED_TOKEN(UNAUTHORIZED, "A-001"),
    INVALID_TOKEN(UNAUTHORIZED, "A-002"),
    INVALID_AUTHORIZATION_HEADER(UNAUTHORIZED, "A-003"),
    NOT_EXIST_AUTHORIZATION(UNAUTHORIZED, "A-004"),
    NOT_BEARER_TYPE_AUTHORIZATION(UNAUTHORIZED, "A-005"),
    ACCESS_TOKEN_NOT_FOUND(UNAUTHORIZED, "A-006"),
    NOT_ACCESS_TOKEN_TYPE(UNAUTHORIZED, "A-007"),
    INVALID_REFRESH_TOKEN(UNAUTHORIZED, "A-008"),
    EXPIRED_REFRESH_TOKEN(UNAUTHORIZED, "A-009"),
    ALREADY_LOGOUT_USER(UNAUTHORIZED, "A-010"),
    NOT_REFRESH_TOKEN_TYPE(UNAUTHORIZED, "A-011"),
    EXPIRED_ACCESS_TOKEN(UNAUTHORIZED, "A-012"),

    /* Oauth2 */
    NOT_SUPPORT_OAUTH2_TYPE(HttpStatus.BAD_REQUEST, "O-001"),
    ALREADY_REGISTERED_FROM_KAKAO(CONFLICT, "O-002"),
    ALREADY_REGISTERED_FROM_GOOGLE(CONFLICT, "O-003"),
    ALREADY_REGISTERED_FROM_NAVER(CONFLICT, "O-004"),
    INVALID_OAUTH2_TYPE(HttpStatus.BAD_REQUEST, "O-005"),

    /* User */
    DUPLICATED_USER(CONFLICT, "M-001"),
    NOT_EXIST_USER(NOT_FOUND, "M-002"),
    NOT_ACCESSIBLE_USER(FORBIDDEN, "M-003"),

    /* Quiz */
    NOT_EXIST_QUIZ(NOT_FOUND, "Q-001"),
    IS_NOT_QUIZ_OWNER(FORBIDDEN, "Q-002"),

    /* Answer */
    NOT_EXIST_ANSWER(NOT_FOUND, "AN-001"),

    /* Vote */
    ALREADY_EXIST_VOTER(CONFLICT, "V-001"),

    /* Common */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "C-001"),
    INTERNAL_ERROR(INTERNAL_SERVER_ERROR, "C-002");

    private final HttpStatus httpStatus;
    private final String code;

    // ErrorMessageInjector를 통해서 메세지를 주입받는다.
    private String message;

    ErrorCode(HttpStatus httpStatus, String code) {
        this.httpStatus = httpStatus;
        this.code = code;
    }

    @Component
    public static class ErrorMessageInjector {
        private final MessageSourceAccessor messageSource;
        private final String defaultLocale;

        private ErrorMessageInjector(MessageSourceAccessor messageSource, @Value("${locale}") String defaultLocale) {
            this.messageSource = messageSource;
            this.defaultLocale = defaultLocale;
        }

        @PostConstruct
        private void init() {
            for (ErrorCode errorCode : ErrorCode.values()) {
                errorCode.message = messageSource.getMessage(errorCode.code, StringUtils.parseLocale(defaultLocale));
            }
        }
    }

}
