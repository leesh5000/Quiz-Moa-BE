package com.leesh.quiz.global.jwt;

import com.leesh.quiz.domain.user.constant.Role;
import com.leesh.quiz.global.constant.LoginUser;
import com.leesh.quiz.global.error.ErrorCode;
import com.leesh.quiz.global.error.exception.AuthenticationException;
import com.leesh.quiz.global.jwt.constant.GrantType;
import com.leesh.quiz.global.jwt.constant.TokenType;
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
public class JwtTokenProvider implements TokenProvider {

    private final String accessTokenExpirationTime;
    private final String refreshTokenExpirationTime;
    private final String tokenSecret;

    public JwtTokenProvider(@Value("${token.access-token-expiration-time}") String accessTokenExpirationTime,
                            @Value("${token.refresh-token-expiration-time}") String refreshTokenExpirationTime,
                            @Value("${token.secret}") String tokenSecret) {
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
        this.tokenSecret = tokenSecret;
    }

    @Override
    public TokenDto createTokenDto(Long userId, Role role) {

        Date accessTokenExpireTime = createAccessTokenExpireTime();
        Date refreshTokenExpireTime = createRefreshTokenExpireTime();

        String accessToken = createAccessToken(userId, role, accessTokenExpireTime);
        String refreshToken = createRefreshToken(userId, refreshTokenExpireTime);

        return TokenDto.builder()
                .grantType(GrantType.BEARER.getType())
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpireTime)
                .refreshToken(refreshToken)
                .refreshTokenExpiresIn(refreshTokenExpireTime)
                .build();
    }

    @Override
    public LoginUser extractUserInfo(String accessToken) throws AuthenticationException {

        Claims claims = extractAllClaims(accessToken);

        // 액세스 토큰이 아니면, 예외 던지기
        if (!TokenType.isAccessToken(claims.getSubject())) {
            throw new AuthenticationException(ErrorCode.NOT_ACCESS_TOKEN_TYPE);
        }

        // 현재 로그인 유저 정보 반환
        return LoginUser.of(
                claims.get("userId", Long.class),
                Role.valueOf(claims.get("role", String.class)));
    }

    @Override
    public void validateRefreshToken(String token) throws AuthenticationException {

        Claims claims = extractAllClaims(token);

        // 리프레시 토큰이 아니면, 예외 던지기
        if (!TokenType.isRefreshToken(claims.getSubject())) {
            throw new AuthenticationException(ErrorCode.NOT_REFRESH_TOKEN_TYPE);
        }

        // 토큰 만료 시간이 지났으면, 예외 던지기
        Date expiration = claims.getExpiration();
        Date now = new Date();
        if (now.after(expiration)) {
            throw new AuthenticationException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }
    }

    @Override
    public String createAccessToken(Long userId, Role role, Date expirationTime) {

        return Jwts.builder()
                .setSubject(TokenType.ACCESS.name())    // 토큰 제목
                .setIssuedAt(new Date())                // 토큰 발급 시간
                .setExpiration(expirationTime)          // 토큰 만료 시간
                .claim("userId", userId)          // 회원 아이디
                .claim("role", role)              // 유저 role
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", "JWT")
                .compact();
    }

    @Override
    public Date createAccessTokenExpireTime() {
        return new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpirationTime));
    }

    private Date createRefreshTokenExpireTime() {
        return new Date(System.currentTimeMillis() + Long.parseLong(refreshTokenExpirationTime));
    }

    private String createRefreshToken(Long userId, Date expirationTime) {

        return Jwts.builder()
                .setSubject(TokenType.REFRESH.name())   // 토큰 제목
                .setIssuedAt(new Date())                // 토큰 발급 시간
                .setExpiration(expirationTime)          // 토큰 만료 시간
                .claim("userId", userId)          // 회원 아이디
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", "JWT")
                .compact();
    }

    private Claims extractAllClaims(String token) throws AuthenticationException {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new AuthenticationException(ErrorCode.EXPIRED_ACCESS_TOKEN);
        } catch (Exception e) {
            throw new AuthenticationException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(tokenSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}

