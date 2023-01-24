package com.leesh.quiz.global.jwt;

import com.leesh.quiz.domain.user.constant.Role;
import com.leesh.quiz.global.constant.LoginUser;
import com.leesh.quiz.global.error.exception.AuthenticationException;

public interface TokenService {

    TokenDto createJwtTokenDto(Long id, Role role);

    LoginUser extractUserInfo(String accessToken) throws AuthenticationException;

}
