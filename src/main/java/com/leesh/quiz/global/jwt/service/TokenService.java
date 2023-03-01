package com.leesh.quiz.global.jwt.service;

import com.leesh.quiz.domain.user.User;
import com.leesh.quiz.global.constant.UserInfo;
import com.leesh.quiz.global.error.exception.AuthenticationException;
import com.leesh.quiz.global.jwt.dto.AccessToken;
import com.leesh.quiz.global.jwt.dto.RefreshToken;

public interface TokenService {

    UserInfo extractUserInfo(String accessToken) throws AuthenticationException;

    void validateRefreshToken(String token) throws AuthenticationException;

    void validateAccessToken(String token) throws AuthenticationException;

    AccessToken createAccessToken(User user);

    RefreshToken createRefreshToken(Long id);

}
