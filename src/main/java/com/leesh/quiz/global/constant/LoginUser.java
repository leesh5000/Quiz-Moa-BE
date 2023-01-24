package com.leesh.quiz.global.constant;

import com.leesh.quiz.domain.user.constant.Role;

public record LoginUser(Long userId, Role role) {

    public static LoginUser of(Long userId, Role role) {
        return new LoginUser(userId, role);
    }

}
