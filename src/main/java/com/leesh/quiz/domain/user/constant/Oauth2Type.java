package com.leesh.quiz.domain.user.constant;

import com.leesh.quiz.global.error.ErrorCode;
import com.leesh.quiz.global.error.exception.BusinessException;

import java.util.Arrays;
import java.util.List;

public enum Oauth2Type {

    KAKAO, NAVER, GOOGLE
    ;

    public static Oauth2Type from(String type) {

        if (!isOauth2Type(type.toUpperCase())) {
            throw new BusinessException(ErrorCode.NOT_SUPPORT_OAUTH2_TYPE);
        }

        return Oauth2Type.valueOf(type.toUpperCase());
    }

    public static boolean isOauth2Type(String type) {

        List<Oauth2Type> oauth2Types = Arrays.stream(Oauth2Type.values())
                .filter(oauth2Type -> oauth2Type.name().equals(type))
                .toList();

        return oauth2Types.size() != 0;
    }


}
