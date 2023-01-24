package com.leesh.quiz.api.home;

import com.leesh.quiz.global.constant.LoginUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/home")
@RestController
public class HomeController {

    @GetMapping()
    public String home(@AuthenticationPrincipal LoginUser loginUser) {
        return "hello";
    }

}
