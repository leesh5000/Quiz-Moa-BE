package com.leesh.quiz.security;

import com.leesh.quiz.domain.user.User;
import com.leesh.quiz.domain.user.UserRepository;
import com.leesh.quiz.domain.user.service.AuthenticationService;
import com.leesh.quiz.dto.AuthenticateDto;
import com.leesh.quiz.dto.RegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenAuthenticationService implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService<String> tokenService;
    private final AuthenticationManager authenticationManager;

    public void register(RegisterDto.Request request) {

        userRepository.findByEmail(request.email())
                .ifPresent(user -> {
                    throw new RuntimeException("Email already exists");
                });

        var user = User.of(request.nickname(), request.email(),
                passwordEncoder.encode(request.password())
        );

        userRepository.save(user);

    }

    public AuthenticateDto.Response authenticate(AuthenticateDto.Request request) {

        // 유저 아이디 및 비밀번호 확인
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        var userDetails = (CustomUserDetails) authenticate.getPrincipal();

        String accessToken = tokenService.generateToken(userDetails);

        return new AuthenticateDto.Response(accessToken);
    }
}
