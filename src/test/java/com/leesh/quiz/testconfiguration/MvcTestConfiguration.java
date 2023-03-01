package com.leesh.quiz.testconfiguration;

import com.leesh.quiz.global.jwt.service.JwtTokenService;
import com.leesh.quiz.global.xss.HtmlCharacterEscapes;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import({JwtTokenService.class,
        HtmlCharacterEscapes.class,
        RestDocsConfiguration.class})
public class MvcTestConfiguration {

}
