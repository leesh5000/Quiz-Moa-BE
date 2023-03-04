package com.leesh.quiz.domain.user.service;

import com.leesh.quiz.domain.user.User;
import com.leesh.quiz.domain.user.UserRepository;
import com.leesh.quiz.external.oauth2.Oauth2Attributes;
import com.leesh.quiz.global.error.ErrorCode;
import com.leesh.quiz.global.error.exception.AuthenticationException;
import com.leesh.quiz.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    public User findUserOrRegister(Oauth2Attributes oauth2Attributes) {

        Optional<User> optionalUser = userRepository.findByEmail(oauth2Attributes.getEmail());
        User user;

        // 해당 이메일로 가입된 회원이 없다면, 신규 가입을 한다.
        if (optionalUser.isEmpty()) {
            User newUser = User.register(oauth2Attributes);
            return userRepository.save(newUser);
        }

        // 해당 이메일로 이미 가입된 회원이라면,
        user = optionalUser.get();

        // 탈퇴한 회원이 아니라면, 로그인 처리 한다.
        if (!user.isDeleted()) {
            user.login(oauth2Attributes);
        } else {
            // 탈퇴했다가 재가입하는 것이라면, 재가입 로직을 수행한다.
            user.reRegister(oauth2Attributes);
        }

        return user;
    }

    @Transactional(readOnly = true)
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_USER));
    }

    @Transactional(readOnly = true)
    public User findUserByRefreshToken(String refreshToken) {

        // 리프레시 토큰을 통해 유저를 찾는다.
        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new AuthenticationException(ErrorCode.INVALID_REFRESH_TOKEN));

        // 현재 시간이 토큰 만료 시간보다 더 미래이면, 토큰이 만료되었다고 판단한다.
        LocalDateTime expiration = user.getRefreshTokenExpiresIn();
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(expiration)) {
            throw new AuthenticationException(ErrorCode.ALREADY_LOGOUT_USER);
        }

        return user;
    }
}
