package com.leesh.quiz.global.jwt.service;

import com.leesh.quiz.domain.user.User;
import com.leesh.quiz.domain.user.constant.Role;
import com.leesh.quiz.global.constant.UserInfo;
import com.leesh.quiz.global.error.ErrorCode;
import com.leesh.quiz.global.error.exception.AuthenticationException;
import com.leesh.quiz.global.jwt.constant.TokenType;
import com.leesh.quiz.global.jwt.dto.AccessToken;
import com.leesh.quiz.global.jwt.dto.RefreshToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Slf4j
@Service
public class JwtTokenService implements TokenService {

    private final Long accessTokenExpirationTime;
    private final Long refreshTokenExpirationTime;
    private final String tokenSecret;

    public JwtTokenService(@Value("${token.access-token-expiration-time}") Long accessTokenExpirationTime,
                           @Value("${token.refresh-token-expiration-time}") Long refreshTokenExpirationTime,
                           @Value("${token.secret}") String tokenSecret) {
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
        this.tokenSecret = tokenSecret;
    }

    @Override
    public UserInfo extractUserInfo(String accessToken) throws AuthenticationException {

        Claims claims = extractAllClaims(accessToken);

        // 접근 토큰이 아니면, 예외 던지기
        if (!TokenType.isAccessToken(claims.getSubject())) {
            throw new AuthenticationException(ErrorCode.NOT_ACCESS_TOKEN_TYPE);
        }

        // 현재 로그인 유저 정보 반환
        return UserInfo.of(
                claims.get("id", Long.class),
                Role.valueOf(claims.get("role", String.class)));
    }

    @Override
    public void validateRefreshToken(String refreshToken) throws AuthenticationException {

        Claims claims = extractAllClaims(refreshToken);

        // 리프레시 토큰이 아니면, 예외 던지기
        if (!TokenType.isRefreshToken(claims.getSubject())) {
            throw new AuthenticationException(ErrorCode.NOT_REFRESH_TOKEN_TYPE);
        }

        // 토큰 만료 시간이 지났으면, 예외 던지기
        Date expiration = claims.getExpiration();
        Date now = new Date();
        if (now.after(expiration)) {
            throw new AuthenticationException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }
    }

    @Override
    public AccessToken createAccessToken(User user) {

        Date expirationTime = new Date(System.currentTimeMillis() + accessTokenExpirationTime);

        String accessToken = Jwts.builder()
                .setSubject(TokenType.ACCESS.name())        // 토큰 제목
                .setIssuedAt(new Date())                    // 토큰 발급 시간
                .setExpiration(expirationTime)              // 토큰 만료 시간
                .claim("id", user.getId())            // 회원 아이디 (PK값)
                .claim("name", user.getUsername())    // 회원 이름
                .claim("email", user.getEmail())      // 회원 이메일
                .claim("role", user.getRole())        // 유저 role
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", "JWT")
                .compact();

        return AccessToken.of(accessToken, (int) (accessTokenExpirationTime / 1000));
    }

    @Override
    public RefreshToken createRefreshToken(Long userId) {

        Date expirationTime = new Date(System.currentTimeMillis() + refreshTokenExpirationTime);

        String refreshToken = Jwts.builder()
                .setSubject(TokenType.REFRESH.name())   // 토큰 제목
                .setIssuedAt(new Date())                // 토큰 발급 시간
                .setExpiration(expirationTime)          // 토큰 만료 시간
                .claim("id", userId)              // 회원 아이디 (PK값)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", "JWT")
                .compact();

        return RefreshToken.of(refreshToken, (int) (refreshTokenExpirationTime / 1000));
    }

    // 이 메소드의 파라미터인 토큰은 해당 시점에서는 Access Token 또는 Refresh Token 인지 알 수 없다.
    private Claims extractAllClaims(String token) throws AuthenticationException {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new AuthenticationException(ErrorCode.EXPIRED_TOKEN);
        } catch (Exception e) {
            throw new AuthenticationException(ErrorCode.INVALID_TOKEN);
        }
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(tokenSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}

