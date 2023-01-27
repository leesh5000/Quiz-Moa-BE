package com.leesh.quiz.api.healthcheck;

import com.leesh.quiz.global.configuration.SecurityConfiguration;
import com.leesh.quiz.global.jwt.service.JwtTokenService;
import com.leesh.quiz.global.xss.HtmlCharacterEscapes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Health check API 테스트")
@WebMvcTest(HealthCheckController.class)
@Import({SecurityConfiguration.class, JwtTokenService.class, HtmlCharacterEscapes.class})
@ActiveProfiles("test")
@AutoConfigureRestDocs
class HealthCheckControllerTest {

    @Autowired
    private MockMvc mvc;

    @DisplayName("정상 호출")
    @Test
    void success_test() throws Exception {

        // when & then
        ResultActions result = mvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.health").value("ok"))
                .andExpect(jsonPath("$.activeProfiles[0]").value("test"))

                .andDo(print());

        // API 문서화
        result.
                andDo(document("health-check",
                        responseFields(
                                fieldWithPath("health").description("현재 서버의 상태"),
                                fieldWithPath("activeProfiles").description("현재 서버의 프로파일"))
                ));
    }

}