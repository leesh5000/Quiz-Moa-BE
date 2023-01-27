package com.leesh.quiz.global.constant;

import com.leesh.quiz.domain.user.constant.Role;

public record UserInfo(Long userId, Role role) {

    public static UserInfo of(Long userId, Role role) {
        return new UserInfo(userId, role);
    }

}
