package com.leesh.quiz.controller;

import com.leesh.quiz.dto.UserJoinDto;
import com.leesh.quiz.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<Void> join(@RequestBody UserJoinDto dto) {

        userService.join(dto);
        return ResponseEntity.ok().build();

    }

}
