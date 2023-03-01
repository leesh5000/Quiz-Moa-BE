package com.leesh.quiz.global.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leesh.quiz.global.interceptor.AuthenticationInterceptor;
import com.leesh.quiz.global.resolver.LoginUserArgumentResolver;
import com.leesh.quiz.global.xss.HtmlCharacterEscapes;
import com.leesh.quiz.global.xss.XssFilter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final ObjectMapper objectMapper;
    private final XssFilter xssFilter;
    private final HtmlCharacterEscapes htmlCharacterEscapes;
    private final AuthenticationInterceptor authenticationInterceptor;
    private final LoginUserArgumentResolver loginUserArgumentResolver;

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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(authenticationInterceptor)
                .order(1)
                .addPathPatterns("/api/**");

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolver);
    }

    // x-www-form-urlencoded 방식의 XSS 방지
    @Bean
    public FilterRegistrationBean<XssFilter> filterRegistrationBean() {
        FilterRegistrationBean<XssFilter> filterRegistration = new FilterRegistrationBean<>(xssFilter);
        filterRegistration.addUrlPatterns("/*"); // 모든 요청에 대해 필터 적용
        filterRegistration.setOrder(0);
        return filterRegistration;
    }

/*
    // FIXME : 프론트 엔드의 QUILL 에디터에서, HTML 태그가 포함된 데이터를 처리하기 때문에 서버에서 가공해서 데이터를 전달해주면 에디터에서 데이터를 렌더링하지 못함. 현재는 프론트에서 XSS를 처리하도록 수정하였기 때문에 주석 처리 함.
    // Body의 raw 데이터로 들어오는 XSS 방지
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(jsonEscapeConverter());
    }
    @Bean
    public MappingJackson2HttpMessageConverter jsonEscapeConverter() {
        ObjectMapper copy = objectMapper.copy();
        copy.getFactory().setCharacterEscapes(htmlCharacterEscapes);
        return new MappingJackson2HttpMessageConverter(copy);
    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }
*/
}
