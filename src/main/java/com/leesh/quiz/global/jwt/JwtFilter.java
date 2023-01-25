package com.leesh.quiz.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leesh.quiz.global.constant.LoginUser;
import com.leesh.quiz.global.error.ErrorResponse;
import com.leesh.quiz.global.error.exception.AuthenticationException;
import com.leesh.quiz.global.jwt.constant.GrantType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {

            final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

            // 인증이 필요한 요청이 아니면, 다음 필터로 이동
            if (!isRequireAuthenticate(header)) {
                filterChain.doFilter(request, response);
                return;
            }

            // Authorization 헤더가 있으면, JWT 토큰 추출하기
            final String token = extractToken(header);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                // JWT 토큰으로 부터 유저 정보 추출하기 (실패시 Authentication 예외 발생)
                LoginUser loginUser = tokenProvider.extractUserInfo(token);

                // SecurityContext 에 인증 정보 저장하기
                setSecurity(request, loginUser);
            }

            filterChain.doFilter(request, response);

        } catch (AuthenticationException e) {

            log.error("AuthenticationException", e);

            // 토큰 검증 과정에서 생긴 예외는 여기서 처리, 나머지는 스프링 시큐리티의 AuthenticationEntryPoint 에서 처리
            sendErrorResponse(response, e);

        }
    }

    private void sendErrorResponse(HttpServletResponse response, AuthenticationException e) throws IOException {

        response.setStatus(e.getErrorCode().getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        ErrorResponse body = ErrorResponse.of(e.getErrorCode().getCode(), e.getMessage());
        objectMapper.writeValue(response.getWriter(), body);
    }

    private static boolean isRequireAuthenticate(String header) {
        // Authorization 헤더가 없거나, Bearer 타입이 아니면, 인증이 필요하지 않음
        return header != null && header.startsWith(GrantType.BEARER.getType());
    }

    private static String extractToken(String header) {
        return header.split(" ")[1].trim();
    }

    private static void setSecurity(HttpServletRequest request, LoginUser loginUser) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                loginUser, null,
                Collections.singleton((GrantedAuthority) () -> loginUser.role().name())
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
