package com.leesh.quiz.api.userprofile.dto.user;

import com.leesh.quiz.domain.user.validation.Username;

public record UsernameDto(@Username String username) {

    public record Response(Long editUserId) {
    }

}
