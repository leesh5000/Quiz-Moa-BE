package com.leesh.quiz.testconfiguration;

import com.leesh.quiz.global.configuration.MessageConfiguration;
import com.leesh.quiz.global.configuration.SecurityConfiguration;
import com.leesh.quiz.global.error.ErrorCode;
import com.leesh.quiz.global.jwt.service.JwtTokenService;
import com.leesh.quiz.global.xss.HtmlCharacterEscapes;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

@TestConfiguration
@Import({SecurityConfiguration.class,
        JwtTokenService.class,
        HtmlCharacterEscapes.class,
        RestDocsConfiguration.class})
@ContextConfiguration(
        initializers = {ConfigDataApplicationContextInitializer.class},
        classes = {MessageConfiguration.class, ErrorCode.ErrorMessageInjector.class}
)
public class MvcTestConfiguration {

    @MockBean
    MessageSource messageSource;

}
