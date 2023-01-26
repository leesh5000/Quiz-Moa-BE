package com.leesh.quiz.global.jwt;

import com.leesh.quiz.domain.user.constant.Role;
import com.leesh.quiz.global.constant.LoginUser;
import com.leesh.quiz.global.error.exception.AuthenticationException;

import java.util.Date;

public interface TokenProvider {

    TokenDto createTokenDto(Long id, Role role);

    LoginUser extractUserInfo(String accessToken) throws AuthenticationException;

    void validateRefreshToken(String token) throws AuthenticationException;

    String createAccessToken(Long id, Role role, Date expirationTime);

    Date createAccessTokenExpireTime();

}
