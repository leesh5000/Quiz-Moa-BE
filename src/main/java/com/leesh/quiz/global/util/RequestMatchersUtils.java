package com.leesh.quiz.global.util;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

public abstract class RequestMatchersUtils {

    private static final RequestMatcher[] permitAllRequestMatchers = new RequestMatcher[]{
            new AntPathRequestMatcher("/api/oauth2/**"),
            new AntPathRequestMatcher("/api/health"),
            toH2Console(),
            PathRequest.toStaticResources().atCommonLocations()
    };

    public static RequestMatcher[] getPermitAllRequestMatchers() {
        return permitAllRequestMatchers;
    }

}
