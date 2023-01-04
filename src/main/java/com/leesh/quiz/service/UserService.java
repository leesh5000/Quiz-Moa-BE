package com.leesh.quiz.service;

import com.leesh.quiz.domain.user.User;
import com.leesh.quiz.domain.user.UserRepository;
import com.leesh.quiz.dto.UserJoinDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    public void join(UserJoinDto dto) {

        userRepository.findByEmail(dto.email())
                .ifPresent(user -> {
                    throw new IllegalStateException("This user already exists.");
                });

        User user = dto.toEntity();
        userRepository.save(user);

    }
}
