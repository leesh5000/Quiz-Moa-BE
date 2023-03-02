package com.leesh.quiz.external.oauth2;

import com.leesh.quiz.domain.user.constant.Oauth2Type;
import com.leesh.quiz.global.error.ErrorCode;
import com.leesh.quiz.global.error.exception.AuthenticationException;

public interface Oauth2ValidationUtils {
    static void isValidOauth2Type(Oauth2Type userOauth2Type, Oauth2Type requestOauth2Type) {
        if (userOauth2Type != requestOauth2Type) {
            switch (userOauth2Type) {
                case GOOGLE -> throw new AuthenticationException(ErrorCode.ALREADY_REGISTERED_FROM_GOOGLE);
                case NAVER -> throw new AuthenticationException(ErrorCode.ALREADY_REGISTERED_FROM_NAVER);
                case KAKAO -> throw new AuthenticationException(ErrorCode.ALREADY_REGISTERED_FROM_KAKAO);
            }
        }
    }

}
