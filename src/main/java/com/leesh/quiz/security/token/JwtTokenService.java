package com.leesh.quiz.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class JwtTokenService implements TokenService<String, UserDetails> {

    private static final String SECRET_KEY = "413F4428472B4B6250655368566D597133743677397A244326452948404D6351";

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24L))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


//    private final Long accessTokenExpiredInMills = 60 * 60 * 1000L; // 1시간
//    private final Long refreshTokenExpiredInMills = 24 * 7 * accessTokenExpiredInMills; // 7일
//
//    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//
//    private final TokenRepository tokenRepository;
//
//    private final CustomUserDetailsService customUserDetailsService;
//
//    @Override
//    public AuthToken create(UserDetails userDetails) {
//
//        String accessToken = createToken(userDetails, Instant.now().plus(accessTokenExpiredInMills, MILLIS));
//        String refreshToken = createToken(userDetails, Instant.now().plus(refreshTokenExpiredInMills, MILLIS));
//
//        // Set refresh token to redis
//        tokenRepository.save(refreshToken, REFRESH_TOKEN, refreshTokenExpiredInMills);
//
//        return new AuthToken(BEARER, accessToken, refreshToken, accessTokenExpiredInMills);
//    }
//
//    @Override
//    public String refresh(UserRefreshDto dto) {
//
//        String value = tokenRepository.get(dto.refreshToken());
//        if (value != null && value.equals(REFRESH_TOKEN)) {
//            UserDetails userDetails = customUserDetailsService.loadUserByUsername(dto.username());
//            // create access token
//            return createToken(userDetails, Instant.now().plus(accessTokenExpiredInMills, MILLIS));
//
//        } else {
//            throw new AccessDeniedException("Invalid refresh token");
//        }
//
//    }
//
//    private String createToken(UserDetails userDetails, Instant expiredAt) {
//        return Jwts.builder()
//                .setIssuer(QUIZ_APP_BACKEND)
//                .setExpiration(from(expiredAt))
//                .claim(USERNAME, userDetails.getUsername())
//                .claim(AUTHORITIES, userDetails.getAuthorities())
//                .signWith(secretKey, SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    @Override
//    public UserDetails getUserDetails(String accessToken) {
//
//        if (isBlacklist(accessToken)) {
//            throw new IllegalArgumentException("You are already logged out. Please log in again.");
//        }
//
//        Claims body = validate(accessToken);
//        String username = body.get(USERNAME, String.class);
//
//        return customUserDetailsService.loadUserByUsername(username);
//    }
//
//    private Claims validate(String accessToken) {
//        Claims body;
//        try {
//            body = Jwts.parserBuilder()
//                    .setSigningKey(secretKey).build()
//                    .parseClaimsJws(accessToken)
//                    .getBody();
//        } catch (ExpiredJwtException e) {
//            throw new AccessDeniedException("This is an expired jwt.");
//        } catch (Exception e) {
//            throw new AccessDeniedException("This is an invalid jwt.");
//        }
//
//        return body;
//    }
//
//    @Override
//    public void invalidateToken(AuthToken authToken) {
//
//        long expiredAt = Jwts.parserBuilder()
//                .setSigningKey(secretKey).build()
//                .parseClaimsJws(authToken.accessToken())
//                .getBody()
//                .getExpiration().getTime();
//
//        // 유저의 액세스 토큰을 Blacklist 에 등록, Timeout = "토큰 만료시간 - 현재시간"
//        Long timeout = Math.max(expiredAt - System.currentTimeMillis(), 0);
//        addBlacklist(authToken, timeout);
//
//        // refresh token 은 redis 에 저장되어 있으므로, redis 에서 삭제
//        tokenRepository.delete(authToken.refreshToken());
//
//    }
//
//    private void addBlacklist(AuthToken authToken, Long timeout) {
//        tokenRepository.save(authToken.accessToken(), ACCESS_TOKEN, timeout);
//    }
//
//    private Boolean isBlacklist(String accessToken) {
//        String value = tokenRepository.get(accessToken);
//        return value != null && value.equals(ACCESS_TOKEN);
//    }
}
