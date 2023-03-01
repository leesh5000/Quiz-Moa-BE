//package com.leesh.quiz.global.jwt;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.leesh.quiz.global.constant.UserInfo;
//import com.leesh.quiz.global.error.dto.ErrorResponse;
//import com.leesh.quiz.global.error.exception.AuthenticationException;
//import com.leesh.quiz.global.jwt.service.TokenService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.Collections;
//
//import static com.leesh.quiz.global.util.AuthorizationHeaderUtils.extractAuthorization;
//import static com.leesh.quiz.global.util.RequestMatchersUtils.isAllowedRequest;
//
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class JwtFilter extends OncePerRequestFilter {
//
//    private final TokenService tokenService;
//    private final ObjectMapper objectMapper;
//
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//        // 허용되는 요청인지 검증
//        return isAllowedRequest(request);
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        try {
//            // Authorization 헤더가 있으면, 헤더 값으로 부터 토큰을 추출 (실패 시 Authentication 예외 발생)
//            String token = extractAuthorization(request);
//
//            if (SecurityContextHolder.getContext().getAuthentication() == null) {
//
//                // JWT 토큰으로 부터 유저 정보 추출하기 (실패시 Authentication 예외 발생)
//                UserInfo userInfo = tokenService.extractUserInfo(token);
//
//                // SecurityContext 에 인증 정보 저장하기
//                setSecurity(request, userInfo);
//            }
//
//            filterChain.doFilter(request, response);
//
//        } catch (AuthenticationException e) {
//
//            log.error("AuthenticationException", e);
//
//            // 토큰 검증 과정에서 생긴 예외는 여기서 처리, 나머지는 스프링 시큐리티의 AuthenticationEntryPoint 에서 처리
//            sendErrorResponse(response, e);
//
//        }
//    }
//
//    private void sendErrorResponse(HttpServletResponse response, AuthenticationException e) throws IOException {
//
//        response.setStatus(e.getErrorCode().getHttpStatus().value());
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
//
//        ErrorResponse body = ErrorResponse.of(e.getErrorCode().getCode(), e.getMessage());
//        objectMapper.writeValue(response.getWriter(), body);
//    }
//
//    private void setSecurity(HttpServletRequest request, UserInfo userInfo) {
//
//        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                userInfo, null,
//                Collections.singleton((GrantedAuthority) () -> userInfo.role().name())
//        );
//
//        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }
//}
