package com.leesh.quiz.security;

import com.leesh.quiz.domain.user.User;
import com.leesh.quiz.domain.user.UserRepository;
import com.leesh.quiz.security.dto.LoginRequest;
import com.leesh.quiz.security.dto.RegisterRequest;
import com.leesh.quiz.security.dto.TokenResponse;
import com.leesh.quiz.security.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService<String, UserDetails> tokenService;
    private final AuthenticationManager authenticationManager;

    public TokenResponse register(RegisterRequest request) {

        userRepository.findByEmail(request.email())
                .ifPresent(user -> {
                    throw new IllegalStateException("already exist email");
                });

        var user = User.of(request.nickname(), request.email(), passwordEncoder.encode(request.password()));
        userRepository.save(user);

        var accessToken = tokenService.generateToken(new CustomUserDetails(user));

        return new TokenResponse(accessToken);
    }

    public TokenResponse authenticate(LoginRequest request) {

        // 유저 아이디 및 비밀번호 확인
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        var user = userRepository.findByEmail(request.email())
                .orElseThrow();

        var accessToken = tokenService.generateToken(new CustomUserDetails(user));

        return new TokenResponse(accessToken);
    }
}
