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

    public User findUserOrRegister(Oauth2Attributes userInfo) {

        Optional<User> optionalUser = userRepository.findByEmail(userInfo.getEmail());
        User user;

        // 해당 이메일로 이미 가입된 회원이라면,
        if (optionalUser.isPresent()) {

            user = optionalUser.get();
            // 현재 로그인 시도한 oauth2 타입과 가입된 회원의 oauth2 타입이 올바른지 검증한다.
            user.isValidOauth2(userInfo.getOauth2Type());

        } else {
            // 해당 이메일로 가입된 회원이 없다면, 신규 가입을 한다.
            isDuplicatedUser(userInfo.getEmail());
            user = userRepository.save(userInfo.toEntity());
        }

        return user;
    }

    @Transactional(readOnly = true)
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_USER));
    }

    @Transactional(readOnly = true)
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
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

    private void isDuplicatedUser(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATED_USER);
        }
    }
}
