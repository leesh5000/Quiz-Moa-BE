package com.leesh.quiz.domain.auth.service;

import com.leesh.quiz.domain.auth.UserInfo;

/**
 * 토큰 인증을 위한 인터페이스 <br>
 * <p>
 *     구체적인 기술에 의존하지 않고 추상화에 의존하기 위함이며,
 *     현재 구현체는 Json Web Token 기술을 사용하고 있습니다.
 * </p>
 * @param <T> 토큰 타입 <br>
 * @see com.leesh.quiz.security.token.jwt.JwtTokenService
 * @link <a href="https://mvnrepository.com/artifact/io.jsonwebtoken">io.jsonwebtoken</a>
 */
public interface TokenService<T> {

    UserInfo extractUserInfo(T token);

    T generateToken(UserInfo userInfo);

    boolean isTokenValid(T token, UserInfo userInfo);

}
