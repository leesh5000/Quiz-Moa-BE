package com.leesh.quiz.global.constant;

import com.leesh.quiz.domain.user.constant.Role;
import lombok.Builder;

@Builder
public record UserInfo(Long userId, String email, String username, Role role) {

}
