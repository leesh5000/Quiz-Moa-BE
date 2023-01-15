package com.leesh.quiz.domain.auth;

import com.leesh.quiz.domain.user.Role;

import java.util.Set;

/**
 * 토큰 생성 및 인증을 위한 유저 정보를 추상화 한 인터페이스
 * <p>
 *     구체적인 기술에 의존하지 않고 추상화에 의존하기 위함
 * </p>
 * Implementation {@link com.leesh.quiz.security.token.jwt.JwtUserInfo}
 */
public interface UserInfo {

    String getUserId();

    String getUsername();

    Set<Role> getAuthorities();

}
