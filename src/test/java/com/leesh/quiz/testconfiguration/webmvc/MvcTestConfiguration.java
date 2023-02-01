package com.leesh.quiz.testconfiguration.webmvc;

import com.leesh.quiz.global.configuration.SecurityConfiguration;
import com.leesh.quiz.global.jwt.service.JwtTokenService;
import com.leesh.quiz.global.xss.HtmlCharacterEscapes;
import com.leesh.quiz.testconfiguration.restdocs.RestDocsConfiguration;
import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import({SecurityConfiguration.class,
        JwtTokenService.class,
        HtmlCharacterEscapes.class,
        RestDocsConfiguration.class,
        H2ConsoleProperties.class})
public class MvcTestConfiguration {
}
