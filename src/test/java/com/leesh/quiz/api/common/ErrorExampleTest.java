package com.leesh.quiz.api.common;

import com.leesh.quiz.api.HomeController;
import com.leesh.quiz.global.configuration.MessageConfiguration;
import com.leesh.quiz.global.error.ErrorCode;
import com.leesh.quiz.testconfiguration.MvcTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HomeController.class)
@Import({MvcTestConfiguration.class, ErrorCode.ErrorMessageInjector.class, MessageConfiguration.class})
@ActiveProfiles("test")
@AutoConfigureRestDocs
public class ErrorExampleTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void createErrorSnippets() throws Exception {

        mvc.perform(get("/api/home"))
                .andExpect(status().is4xxClientError())
                .andDo(document("error-example",
                        responseFields(
                                fieldWithPath("errorCode").type(JsonFieldType.STRING).description("에러 코드"),
                                fieldWithPath("errorMessage").type(JsonFieldType.STRING).description("에러 메시지")
                        )
                ));

    }

}
