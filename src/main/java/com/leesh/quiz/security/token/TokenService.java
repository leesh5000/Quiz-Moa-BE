package com.leesh.quiz.security.token;

/**
 * 토큰 인증을 위한 인터페이스 <br>
 * @param <T> 토큰 타입
 */
public interface TokenService<T> {

    UserInfo extractUserInfo(T token);

    T generateToken(UserInfo userInfo);

    boolean isTokenValid(T token, UserInfo userInfo);

}
