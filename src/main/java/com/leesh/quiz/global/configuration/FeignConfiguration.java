package com.leesh.quiz.global.configuration;

import com.leesh.quiz.global.error.FeignClientErrorDecoder;
import feign.Logger;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Configuration
@EnableFeignClients(basePackages = "com.leesh.quiz")
@Import(FeignClientsConfiguration.class)
public class FeignConfiguration {

    @Bean
    feign.Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignClientErrorDecoder();
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(1000, 2000, 3);
    }

}
