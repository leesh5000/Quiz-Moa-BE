package com.leesh.quiz.security.token.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leesh.quiz.security.token.TokenService;
import com.leesh.quiz.security.token.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class JwtTokenService implements TokenService<String> {

    private static final String SECRET_KEY = "413F4428472B4B6250655368566D597133743677397A244326452948404D6351";
    private final ObjectMapper objectMapper;

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public UserInfo extractUserInfo(String token) {

        Claims claims = extractAllClaims(token);

        return objectMapper.convertValue(claims, JwtUserInfo.class);
    }

    @Override
    public String generateToken(UserInfo userInfo) {

        Map<String, Object> userInfoData = objectMapper.convertValue(userInfo, Map.class);
        var extraClaims = new HashMap<>(userInfoData);

        return generateToken(extraClaims, userInfo);
    }

    @Override
    public boolean isTokenValid(String token, UserInfo userInfo) {
        final String username = extractUserId(token);
        return (username.equals(userInfo.getUserId()) && !isTokenExpired(token));
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

    private String generateToken(Map<String, Object> extraClaims, UserInfo userInfo) {

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userInfo.getUserId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24L))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}