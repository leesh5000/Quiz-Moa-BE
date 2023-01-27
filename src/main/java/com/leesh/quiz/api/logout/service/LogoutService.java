package com.leesh.quiz.api.logout.service;

import com.leesh.quiz.domain.user.User;
import com.leesh.quiz.domain.user.service.UserService;
import com.leesh.quiz.global.constant.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class LogoutService {
    private final UserService userService;

    public void logout(UserInfo userInfo) {

        // 현재 유저 정보로 유저를 찾는다.
        User user = userService.findUserById(userInfo.userId());

        // 유저의 리프레시 토큰을 만료처리한다.
        user.expireRefreshToken();

    }
}
