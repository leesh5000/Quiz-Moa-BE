package com.leesh.quiz.controller;

import com.leesh.quiz.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

}
