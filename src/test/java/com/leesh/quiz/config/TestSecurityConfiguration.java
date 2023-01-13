package com.leesh.quiz.config;

import com.leesh.quiz.domain.user.UserRepository;
import com.leesh.quiz.security.SecurityConfiguration;
import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@Import({SecurityConfiguration.class})
public class TestSecurityConfiguration {

    @MockBean
    UserRepository userRepository;

    // PathRequest 클래스의 toH2Console()가 설정되지 않는 이슈로 인해 추가
    @MockBean
    H2ConsoleProperties h2ConsoleProperties;

}
