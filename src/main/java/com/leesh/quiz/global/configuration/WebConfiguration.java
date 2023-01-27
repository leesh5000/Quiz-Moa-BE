package com.leesh.quiz.global.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leesh.quiz.global.xss.HtmlCharacterEscapes;
import com.leesh.quiz.global.xss.XssFilter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final ObjectMapper objectMapper;
    private final XssFilter xssFilter;
    private final HtmlCharacterEscapes htmlCharacterEscapes;

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.PATCH.name(),
                        HttpMethod.DELETE.name(),
                        HttpMethod.OPTIONS.name())
                .maxAge(3600);

    }

    // Body의 raw 데이터로 들어오는 XSS 방지
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(jsonEscapeConverter());
    }

    // x-www-form-urlencoded 방식의 XSS 방지
    @Bean
    public FilterRegistrationBean<XssFilter> filterRegistrationBean() {
        FilterRegistrationBean<XssFilter> filterRegistration = new FilterRegistrationBean<>(xssFilter);
        filterRegistration.addUrlPatterns("/*"); // 모든 요청에 대해 필터 적용
        filterRegistration.setOrder(0);
        return filterRegistration;
    }

    @Bean
    public MappingJackson2HttpMessageConverter jsonEscapeConverter() {
        ObjectMapper copy = objectMapper.copy();
        copy.getFactory().setCharacterEscapes(htmlCharacterEscapes);
        return new MappingJackson2HttpMessageConverter(copy);
    }

}
