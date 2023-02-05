package com.leesh.quiz.global.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;

public interface RequestMatchersUtils {

    RequestMatcher[] permitAllRequestMatchers = new RequestMatcher[]{
            new AntPathRequestMatcher("/api/oauth2/**"),
            new AntPathRequestMatcher("/api/health"),
            new AntPathRequestMatcher("/api/access-token/refresh"),
            new AntPathRequestMatcher("/docs/**"),
            // 퀴즈 조회는 비회원 유저도 가능
            new AntPathRequestMatcher("/api/quizzes/**", HttpMethod.GET.name()),
            PathRequest.toStaticResources().atCommonLocations()
    };

    static boolean isAllowedRequest(HttpServletRequest request) {
        return Arrays.stream(permitAllRequestMatchers)
                .anyMatch(requestMatcher -> requestMatcher.matches(request));
    }

}
