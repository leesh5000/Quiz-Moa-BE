package com.leesh.quiz.api.tokenrefresh.service;

import com.leesh.quiz.api.tokenrefresh.dto.TokenRefreshDto;
import com.leesh.quiz.domain.user.User;
import com.leesh.quiz.domain.user.service.UserService;
import com.leesh.quiz.global.jwt.dto.AccessToken;
import com.leesh.quiz.global.jwt.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenRefreshService {

    private final TokenService tokenService;
    private final UserService userService;

    public TokenRefreshDto refresh(String refreshToken) {

        // 리프레시 토큰 검증하기
        tokenService.validateRefreshToken(refreshToken);

        // 검증을 통과했으면, 리프레시 토큰을 통해 유저를 찾는다.
        // 추후 리프레시 토큰이 탈취 되었을 때 블락 처리를 할 수 있어야 하기 때문에 DB에서 유저를 찾는다.
        User user = userService.findUserByRefreshToken(refreshToken);

        // 새로운 액세스 토큰을 발급한다.
        AccessToken accessToken = tokenService.createAccessToken(user);

        return TokenRefreshDto.from(accessToken);

    }
}
