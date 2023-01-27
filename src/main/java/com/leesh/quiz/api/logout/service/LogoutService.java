package com.leesh.quiz.api.logout.service;

import com.leesh.quiz.domain.user.UserRepository;
import com.leesh.quiz.domain.user.service.UserService;
import com.leesh.quiz.global.constant.UserInfo;
import com.leesh.quiz.global.jwt.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LogoutService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final UserService userService;

    public void logout(String accessToken) {

        // 접근 토큰 검증하기
        tokenService.validateAccessToken(accessToken);

        // 검증을 통과했으면, 접근 토큰을 통해 유저를 찾는다.
        UserInfo userInfo = tokenService.extractUserInfo(accessToken);

    }
}
