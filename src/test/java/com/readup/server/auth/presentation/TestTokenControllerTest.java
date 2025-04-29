package com.readup.server.auth.presentation;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.readup.server.AbstractWebMvcTest;
import com.readup.server.auth.application.TestTokenService;
import com.readup.server.auth.dto.AuthTokens;

@WebMvcTest(TestTokenController.class)
@AutoConfigureMockMvc(addFilters = false)
class TestTokenControllerTest extends AbstractWebMvcTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private TestTokenService testTokenService;

	@Test
	@DisplayName("테스트 토큰 컨트롤러 테스트")
	void generateTestTokens() throws Exception {
		final String uri = "/public/test/tokens";

		AuthTokens expectedTokens = new AuthTokens("access-token-value", "refresh-token-value");

		when(testTokenService.getTestToken()).thenReturn(expectedTokens);

		mockMvc.perform(get(uri)
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.data.accessToken").value("access-token-value"))
			.andExpect(jsonPath("$.data.refreshToken").value("refresh-token-value"))
			.andExpect(jsonPath("$.message").value("Test tokens generated successfully"))
			.andDo(document("test-tokens-generate",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				responseFields(
					fieldWithPath("success").description("성공 여부"),
					fieldWithPath("data.accessToken").description("액세스 토큰"),
					fieldWithPath("data.refreshToken").description("리프레시 토큰"),
					fieldWithPath("message").description("응답 메시지")
				)
			));
	}
}