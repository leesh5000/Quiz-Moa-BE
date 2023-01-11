package com.leesh.quiz.security.token;

import com.leesh.quiz.domain.user.Role;

import java.util.Set;

/**
 * 토큰 생성 및 인증을 위한 유저 정보를 추상화 한 인터페이스
 */
public interface UserInfo {

    String getUserId();

    String getUsername();

    Set<Role> getAuthorities();

}