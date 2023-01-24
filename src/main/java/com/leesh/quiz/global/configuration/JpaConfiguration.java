package com.leesh.quiz.global.configuration;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EnableJpaAuditing
@Configuration
public class JpaConfiguration {

    @Bean
    public AuditorAware<String> auditorAware() {

        return () -> {

            String modifiedBy = "unknown";

            if (RequestContextHolder.getRequestAttributes() != null) {

                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                        .getRequestAttributes())
                        .getRequest();

                // 생성자, 수정자에 Reuqest URI를 넣어준다.
                if (StringUtils.hasText(request.getRequestURI())) {
                    modifiedBy = request.getRequestURI();
                }
            }

            return Optional.of(modifiedBy);
        };
    }

}
