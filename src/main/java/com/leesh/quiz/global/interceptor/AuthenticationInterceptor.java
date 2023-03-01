package com.leesh.quiz.global.interceptor;

import com.leesh.quiz.global.constant.UserInfo;
import com.leesh.quiz.global.error.exception.AuthenticationException;
import com.leesh.quiz.global.jwt.service.TokenService;
import com.leesh.quiz.global.util.AuthorizationHeaderUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;
    private final String[] allowedPaths = {
            "/api/access-token/refresh",
            "/api/oauth2/**",
            "/api/health",
            "/docs/**",
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws AuthenticationException {

        // 1. 인증이 필요 없는 요청인지 확인
        if (isPublicRequest(request)) {
            return true;
        }

        // 2. Authorization Header 추출 및 검증
        String accessToken = AuthorizationHeaderUtils.extractAuthorization(request);

        // 3. 토큰 검증
        tokenService.validateAccessToken(accessToken);

        // 4. 유저 정보 추출
        UserInfo userInfo = tokenService.extractUserInfo(accessToken);

        // 5. 유저 정보를 Request에 저장
        request.setAttribute(UserInfo.class.getName(), userInfo);

        return true;
    }

    private boolean isPublicRequest(HttpServletRequest request) {

        AntPathMatcher matcher = new AntPathMatcher();

        // 현대 브라우저의 경우, Cross-Origin 요청에 대해서 브라우저 스스로 이 요청이 안전한지 서버에 확인 받기 위해 Preflight 요청을 먼저 보낸다.
        // Preflight 요청은 OPTIONS 메서드로, 실제 요청과는 다른 요청을 보내기 때문에 인증 헤더가 존재하지 않는다.
        // 따라서, 모든 OPTIONS 메서드를 허용 시켜준다.
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        // Request URI에서 컨텍스트 경로를 제거한다.
        final String requestURI = request.getRequestURI().substring(request.getContextPath().length());

        // 퀴즈 조회는 인증이 필요 없음
        if (matcher.match("/api/quizzes/**", requestURI) &&
                (request.getMethod().equals("GET"))) {
            return true;
        }

        for (String allowedPath : allowedPaths) {
            if (matcher.match(allowedPath, requestURI)) {
                return true;
            }
        }

        return false;
    }
}
