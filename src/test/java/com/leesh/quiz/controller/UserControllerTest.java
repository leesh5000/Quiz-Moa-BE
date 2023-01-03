package com.leesh.quiz.controller;

import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("유저 API 엔드포인트 테스트")
@WebMvcTest
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

}