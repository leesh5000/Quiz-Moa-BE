package com.leesh.quiz.common;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;

@RequiredArgsConstructor
public class Constants {

    public static final PasswordEncoder encoder = createDelegatingPasswordEncoder();

}
