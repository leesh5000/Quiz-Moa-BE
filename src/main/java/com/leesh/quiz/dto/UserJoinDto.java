package com.leesh.quiz.dto;

import com.leesh.quiz.domain.user.User;

public record UserJoinDto(String email, String username, String password) {

    public static UserJoinDto of(String email, String username, String password) {
        return new UserJoinDto(email, username, password);
    }

    public User toEntity() {
        return User.of(email, username, password);
    }

}
