package com.leesh.quiz.global.jwt;

import com.leesh.quiz.domain.user.constant.Role;

public interface TokenService {

    TokenDto createJwtTokenDto(Long id, Role role);

}
