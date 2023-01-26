package com.leesh.quiz.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leesh.quiz.global.constant.LoginUser;
import com.leesh.quiz.global.error.ErrorResponse;
import com.leesh.quiz.global.error.exception.AuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

import static com.leesh.quiz.global.util.AuthorizationHeaderUtils.extractToken;
import static com.leesh.quiz.global.util.AuthorizationHeaderUtils.validateAuthorization;
import static com.leesh.quiz.global.util.RequestMatchersUtils.getPermitAllRequestMatchers;

@Slf4j
@RequiredArgsConstructor()
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        // permitAll() 설정된 요청은 인증 필터를 거치지 않음
        return Arrays.stream(getPermitAllRequestMatchers())
                .anyMatch(requestMatcher -> requestMatcher.matches(request));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {

            // Authorization 헤더 검증
            validateAuthorization(request);

            // Authorization 헤더가 있으면, 헤더 값으로 부터 토큰을 추출 (실패 시 Authentication 예외 발생)
            String token = extractToken(request);

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

    private void setSecurity(HttpServletRequest request, LoginUser loginUser) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                loginUser, null,
                Collections.singleton((GrantedAuthority) () -> loginUser.role().name())
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
