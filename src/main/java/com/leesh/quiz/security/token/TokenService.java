package com.leesh.quiz.security.token;

/**
 * 토큰 인증을 위한 인터페이스 <br>
 * @param <T> 토큰 타입
 * @param <V> 유저 정보 타입
 */
public interface TokenService<T, V> {

    T extractUsername(T token);

    T generateToken(V userInfo);

    boolean isTokenValid(T token, V userInfo);

}
