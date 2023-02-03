package com.leesh.quiz.global.validator;

import com.leesh.quiz.global.constant.UserInfo;
import com.leesh.quiz.global.error.ErrorCode;
import com.leesh.quiz.global.error.exception.BusinessException;

import java.util.Objects;

public interface UserInfoValidator {

    static void isAccessible(Long pathParameter, UserInfo userInfo) throws BusinessException {
        if (!Objects.equals(pathParameter, userInfo.userId())) {
            throw new BusinessException(ErrorCode.NOT_ACCESSIBLE_USER);
        }
    }

}
