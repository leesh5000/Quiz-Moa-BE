package com.leesh.quiz.global.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class TimezoneConfiguration {

    protected TimezoneConfiguration(@Value("${custom.timezone}") String timezone) {
        TimeZone.setDefault(TimeZone.getTimeZone(timezone));
    }

}
