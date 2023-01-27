package com.leesh.quiz.global.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

public interface RequestMatchersUtils {

    RequestMatcher[] permitAllRequestMatchers = new RequestMatcher[]{
            new AntPathRequestMatcher("/api/oauth2/**"),
            new AntPathRequestMatcher("/api/health"),
            new AntPathRequestMatcher("/api/access-token/refresh"),
            new AntPathRequestMatcher("/docs/**"),
            toH2Console(),
            PathRequest.toStaticResources().atCommonLocations()
    };

    static boolean isAllowedRequest(HttpServletRequest request) {
        return Arrays.stream(permitAllRequestMatchers)
                .anyMatch(requestMatcher -> requestMatcher.matches(request));
    }

}
