package com.readup.server.common.exception;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.readup.server.common.config.WebConfig;
import com.readup.server.common.resolver.CurrentAuthUserArgumentResolver;
import com.readup.server.common.resolver.CurrentSocialAccountArgumentResolver;
import com.readup.server.common.resolver.CurrentUserArgumentResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.readup.server.AbstractWebMvcTest;
import com.readup.server.common.controller.TestCommonController;

@WebMvcTest(
		controllers = TestCommonController.class,
		excludeFilters = @ComponentScan.Filter(
				type = FilterType.ASSIGNABLE_TYPE,
				classes = {
						WebConfig.class,
						CurrentUserArgumentResolver.class,
						CurrentSocialAccountArgumentResolver.class,
						CurrentAuthUserArgumentResolver.class
				}
		)
)
@AutoConfigureMockMvc(addFilters = false)
class GlobalExceptionHandlerTest extends AbstractWebMvcTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@BeforeEach
	void setUp(RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
			.apply(documentationConfiguration(restDocumentation))
			.build();
	}

	@Test
	@DisplayName("컨트롤러 예외 발생 시 올바른 에러 응답 반환")
	void whenControllerExceptionThrown_thenReturnsProperErrorResponse() throws Exception {
		mockMvc.perform(get("/test/controller"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.error").value("INVALID_REQUEST"))
			.andExpect(jsonPath("$.message").value("Controller exception occurred"))
			.andDo(document("common/exception/controller-exception"));
	}

	@Test
	@DisplayName("서비스 예외 발생 시 올바른 에러 응답 반환")
	void whenServiceExceptionThrown_thenReturnsProperErrorResponse() throws Exception {
		mockMvc.perform(get("/test/service"))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.error").value("RESOURCE_NOT_FOUND"))
			.andExpect(jsonPath("$.message").value("Service exception occurred"))
			.andDo(document("common/exception/service-exception"));
	}

	@Test
	@DisplayName("레포지토리 예외 발생 시 올바른 에러 응답 반환")
	void whenRepositoryExceptionThrown_thenReturnsProperErrorResponse() throws Exception {
		mockMvc.perform(get("/test/repository"))
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("$.error").value("INTERNAL_SERVER_ERROR"))
			.andExpect(jsonPath("$.message").value("Repository exception occurred"))
			.andDo(document("common/exception/repository-exception"));
	}
}
