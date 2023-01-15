package com.leesh.quiz.security;

import com.leesh.quiz.security.jwt.JwtTokenService;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 토큰 인증을 위한 인터페이스
 * <p>
 *     토큰 인증에 대한 구체적인 기술에 의존하지 않기 위함이며, 현재 구현체는 Json Web Token 기술을 사용하는 {@link JwtTokenService} 입니다.
 * </p>
 * @param <T> 토큰 타입 <br>
 * @link <a href="https://mvnrepository.com/artifact/io.jsonwebtoken">io.jsonwebtoken</a>
 */
public interface TokenService<T> {

    UserDetails extractUserDetails(T token);

    T generateToken(UserDetails userDetails);

    boolean isTokenValid(T token, UserDetails userDetails);

}
