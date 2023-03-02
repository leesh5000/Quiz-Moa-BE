package com.leesh.quiz.testconfiguration;

import com.leesh.quiz.global.configuration.LocaleConfiguration;
import com.leesh.quiz.global.configuration.MessageConfiguration;
import com.leesh.quiz.global.jwt.service.JwtTokenService;
import com.leesh.quiz.global.xss.HtmlCharacterEscapes;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.Locale;

@TestConfiguration
@Import({JwtTokenService.class,
        HtmlCharacterEscapes.class,
        RestDocsConfiguration.class,
        MessageConfiguration.class
})
public class MvcTestConfiguration {

    @Bean
    public LocaleConfiguration localeConfiguration() {
        return new LocaleConfiguration(Locale.getDefault().toString());
    }

}
