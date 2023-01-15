package com.leesh.quiz.domain.auth.service;

import com.leesh.quiz.domain.user.User;
import com.leesh.quiz.domain.user.UserRepository;
import com.leesh.quiz.dto.AuthenticateDto;
import com.leesh.quiz.dto.RegisterDto;
import com.leesh.quiz.security.token.jwt.JwtUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService<String> tokenService;
    private final AuthenticationManager authenticationManager;

    public RegisterDto.Response register(RegisterDto.Request request) {

        userRepository.findByEmail(request.email())
                .ifPresent(user -> {
                    throw new RuntimeException("Email already exists");
                });

        var user = User.of(
                request.nickname(),
                request.email(),
                passwordEncoder.encode(
                        request.password()
                )
        );

        userRepository.save(user);

        var accessToken = generateToken(user);

        return RegisterDto.Response.of(accessToken);
    }

    public AuthenticateDto.Response authenticate(AuthenticateDto.Request request) {

        // 유저 아이디 및 비밀번호 확인
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        var user = userRepository.findByEmail(request.email())
                .orElseThrow();

        var accessToken = generateToken(user);

        return new AuthenticateDto.Response(accessToken);
    }

    private String generateToken(User user) {
        return tokenService.generateToken(
                JwtUserInfo.of(
                        user.getEmail(),
                        user.getNickname(),
                        Set.of(user.getRole())
                )
        );
    }
}
