package com.leesh.quiz.configuration;

import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.operation.preprocess.HeadersModifyingOperationPreprocessor;
import org.springframework.restdocs.operation.preprocess.Preprocessors;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;

@TestConfiguration
public class TestRestDocsConfiguration {

    @Bean
    public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer() {

        var removeHeaders = new HeadersModifyingOperationPreprocessor()
                .remove("Vary")
                .remove("X-Content-Type-Options")
                .remove("X-XSS-Protection")
                .remove("X-Frame-Options")
                .remove("Cache-Control")
                .remove("Pragma")
                .remove("Expires");

        return configurer -> configurer.operationPreprocessors()
                .withRequestDefaults(
                        modifyUris()
                                .scheme("https")
                                .host("docs.api.com")
                                .removePort(),
                        Preprocessors.prettyPrint())
                .withResponseDefaults(
                        removeHeaders,
                        Preprocessors.prettyPrint());
    }

}
