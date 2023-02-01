package com.leesh.quiz.testconfiguration.restdocs;

import com.leesh.quiz.api.HomeController;
import com.leesh.quiz.testconfiguration.webmvc.TestControllerConfiguration;
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

@WebMvcTest(HomeController.class)
@Import(TestControllerConfiguration.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs
public class ErrorExampleTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void createErrorSnippets() throws Exception {

        mvc.perform(get("/api/home"))
                .andDo(document("error-example",
                        responseFields(
                                fieldWithPath("errorCode").type(JsonFieldType.STRING).description("에러 코드"),
                                fieldWithPath("errorMessage").type(JsonFieldType.STRING).description("에러 메시지")
                        )
                ));

    }

}
