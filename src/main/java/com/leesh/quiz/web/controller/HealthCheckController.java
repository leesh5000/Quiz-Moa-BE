package com.leesh.quiz.web.controller;

import com.leesh.quiz.web.dto.HealthCheckDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class HealthCheckController {

    private final Environment environment;

    @GetMapping("/health")
    public ResponseEntity<HealthCheckDto.Response> healthCheck() {

        return ResponseEntity.ok(
                HealthCheckDto.Response.of(
                        "OK",
                        List.of(environment.getActiveProfiles())));
    }

}
