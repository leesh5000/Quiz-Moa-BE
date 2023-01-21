package com.leesh.quiz.global.configuration.jpa;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.AuditorAware;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        String modifiedBy = "unknown";

        if (RequestContextHolder.getRequestAttributes() != null) {

            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes())
                    .getRequest();

            // 생성자, 수정자에 Reuqest URI를 넣어준다.
            if (!StringUtils.hasText(request.getRequestURI())) {
                modifiedBy = request.getRequestURI();
            }
        }

        return Optional.of(modifiedBy);
    }

}
