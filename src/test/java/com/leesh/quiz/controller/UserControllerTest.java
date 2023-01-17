package com.leesh.quiz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leesh.quiz.configuration.TestRestDocsConfiguration;
import com.leesh.quiz.configuration.TestSecurityConfiguration;
import com.leesh.quiz.service.UserService;
import com.leesh.quiz.web.controller.UserController;
import com.leesh.quiz.web.dto.CreateQuizDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("UserController Web Layer 테스트")
@Import({TestSecurityConfiguration.class, TestRestDocsConfiguration.class})
@AutoConfigureRestDocs
@WebMvcTest(UserController.class)
class UserControllerTest {

    private final MockMvc mvc;

    private final ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    public UserControllerTest(@Autowired MockMvc mvc,
                              @Autowired ObjectMapper objectMapper) {

        this.mvc = mvc;
        this.objectMapper = objectMapper;
    }

    @DisplayName("[createQuiz 성공 테스트] createQuiz 메소드가 요청되면, 반환 값에는 200 상태코드와 quiz Id가 존재해야 한다.")
    @Test
    public void createQuiz_success_test() throws Exception {

        // given 1 : CreateQuizRequest 객체 생성
        var request = CreateQuizDto.Request.of("title", "content");

        // given 2 : userService.createQuiz() 메소드는 CreateQuizResponse 객체를 반환한다.
        var username = "test";
        var createQuizId = 1L;
        given(userService.createQuiz(request, username))
                .willReturn(CreateQuizDto.Response.of(createQuizId));

        // when & then 1 : createQuiz 메소드가 요청되면, 반환 값에는 200 상태코드와 quiz Id가 존재해야 한다.
        mvc.perform(
                        post("/api/v1/user/{username}/quiz", username)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer {AccessToken}")
                                .content(
                                        objectMapper.writeValueAsString(request)
                                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.createQuizId").value(createQuizId))
                .andDo(print())

                // API 문서화
                .andDo(document("createQuiz",
                        pathParameters(
                                parameterWithName("username").description("유저 닉네임")),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("퀴즈 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("퀴즈 내용")),
                        responseFields(
                                fieldWithPath("createQuizId").type(JsonFieldType.NUMBER).description("생성된 퀴즈 Id")
                        )));
        ;

        // then 2 : userService.createQuiz() 메소드가 호출되어야 한다.
        then(userService).should().createQuiz(request, username);

    }

    @DisplayName("[createQuiz 실패 테스트] URL 경로 상의 {username}과 현재 로그인 한 유저의 username이 다른 경우에는 IllegalStateException 예외가 발생해야 한다.")
    @Test
    public void createQuiz_fail_test_1() throws Exception {

    }

}