package com.leesh.quiz.security.token.jwt;

import com.leesh.quiz.domain.user.Role;
import com.leesh.quiz.security.token.UserInfo;

import java.util.Set;

public record JwtUserInfo(String userId, String username, Set<Role> authorities) implements UserInfo {
    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Set<Role> getAuthorities() {
        return authorities;
    }
}
