package com.leesh.quiz.web.dto;

import java.util.List;

public interface HealthCheckDto {

    record Response(String health, List<String> activeProfiles) {

        public static Response of(String health, List<String> activeProfiles) {
            return new Response(health, activeProfiles);
        }

    }

}
