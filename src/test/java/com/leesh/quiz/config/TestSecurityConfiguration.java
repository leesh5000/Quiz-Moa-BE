package com.leesh.quiz.config;

import com.leesh.quiz.domain.user.UserRepository;
import com.leesh.quiz.security.SecurityConfiguration;
import com.leesh.quiz.security.token.TokenService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@Import({SecurityConfiguration.class})
public class TestSecurityConfiguration {

    @MockBean
    private TokenService<?> tokenService;

    @MockBean
    UserRepository userRepository;

}
