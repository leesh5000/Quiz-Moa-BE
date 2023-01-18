package com.leesh.quiz.configuration;

import com.leesh.quiz.domain.user.Role;
import com.leesh.quiz.domain.user.UserRepository;
import com.leesh.quiz.global.configuration.SecurityConfiguration;
import com.leesh.quiz.service.TokenService;
import com.leesh.quiz.web.dto.CustomUserDetails;
import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@Import({SecurityConfiguration.class})
public class TestSecurityConfiguration {

    @MockBean
    UserRepository userRepository;

    // PathRequest 클래스의 toH2Console()가 설정되지 않는 이슈로 인해 추가
    @MockBean
    H2ConsoleProperties h2ConsoleProperties;

    @MockBean
    TokenService<String> tokenService;

    @BeforeTestMethod
    public void setup() {

        given(tokenService.extractUserDetails(anyString()))
                .willReturn(
                        CustomUserDetails.of("test", "", Role.USER));

        given(tokenService.isTokenValid(anyString(), any(UserDetails.class)))
                .willReturn(true);

    }

}
