package com.leesh.quiz.global.util;

import com.leesh.quiz.global.error.ErrorCode;
import com.leesh.quiz.global.error.exception.AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import static com.leesh.quiz.global.jwt.constant.GrantType.isBearerType;

public interface AuthorizationHeaderUtils {

    static String extractAuthorization(HttpServletRequest request) throws AuthenticationException {

        // Authorization 헤더가 없으면 예외 발생
        validateAuthorization(request);

        String[] authorizations = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ");

         // Authorization 헤더가 Bearer 타입이 아니거나, 토큰이 없으면 예외 발생
        if (authorizations.length != 2) {

            // Bearer 타입이 아니면 예외 발생
            if (!isBearerType(authorizations[0])) {
                throw new AuthenticationException(ErrorCode.NOT_BEARER_TYPE_AUTHORIZATION);
            }

            // Bearer 타입은 맞는데, 토큰이 없는 경우
            throw new AuthenticationException(ErrorCode.NOT_EXIST_AUTHORIZATION);
        }

        return authorizations[1];
    }

    static void validateAuthorization(HttpServletRequest request) {

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!StringUtils.hasText(authorization)) {
            throw new AuthenticationException(ErrorCode.NOT_EXIST_AUTHORIZATION);
        }

    }

}
