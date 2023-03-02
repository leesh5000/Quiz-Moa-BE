package com.leesh.quiz.global.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Locale;

@Configuration
public class LocaleConfiguration {

    public LocaleConfiguration(@Value("${custom.locale}") String locale) {
        Locale defaultLocale = StringUtils.parseLocale(locale);
        assert defaultLocale != null;
        Locale.setDefault(defaultLocale);
    }

}
