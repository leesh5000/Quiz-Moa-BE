package com.leesh.quiz.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leesh.quiz.global.constant.LoginUser;
import com.leesh.quiz.global.error.ErrorCode;
import com.leesh.quiz.global.error.ErrorResponse;
import com.leesh.quiz.global.error.exception.AuthenticationException;
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
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

import static com.leesh.quiz.global.jwt.constant.GrantType.isBearerType;
import static com.leesh.quiz.global.util.RequestMatchersUtils.getPermitAllRequestMatchers;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        // 현재 요청이 인증 확인이 필요 없는 요청인지 확인하기
        RequestMatcher[] whitelists = getPermitAllRequestMatchers();

        return Arrays.stream(whitelists)
                .anyMatch(o -> o.matches(request));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {

            final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

            // 인증 헤더로 부터 토큰 추출 (실패시 Authentication 예외 발생)
            final String token = extractTokenFromAuthorization(header);

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

    private String extractTokenFromAuthorization(String header) throws AuthenticationException {

        // Authorization 헤더가 없으면 예외 발생
        if (!StringUtils.hasText(header)) {
            throw new AuthenticationException(ErrorCode.NOT_EXIST_AUTHORIZATION);
        }

        String[] authorizations = header.split(" ");

        // Authorization 헤더가 Bearer 타입이 아니면 예외 발생
        if(authorizations.length < 2 || (!isBearerType(authorizations[0]))) {
            throw new AuthenticationException(ErrorCode.NOT_BEARER_TYPE_AUTHORIZATION);
        }

        return authorizations[1];
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
