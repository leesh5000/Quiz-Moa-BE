package com.leesh.quiz.global.configuration.jpa;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@RequiredArgsConstructor
@EnableJpaAuditing
@Configuration
public class JpaConfiguration {

    private final HttpServletRequest httpServletRequest;

    @Bean
    public AuditorAware<String> auditorAware() {

        // 데이터 수정자에 요청 URI를 넣기
        String modifiedBy = Optional.ofNullable(
                httpServletRequest.getRequestURI())
                .orElse("unknown");

        return () -> Optional.of(modifiedBy);
    }

}
