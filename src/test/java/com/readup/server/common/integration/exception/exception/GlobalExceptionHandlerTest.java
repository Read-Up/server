package com.readup.server.common.integration.exception.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import com.readup.server.common.integration.controller.TestCommonController;

@WebMvcTest(TestCommonController.class)
@AutoConfigureMockMvc(addFilters = false)
public class GlobalExceptionHandlerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("컨트롤러 예외 발생 시 올바른 에러 응답 반환")
	public void whenControllerExceptionThrown_thenReturnsProperErrorResponse() throws Exception {
		mockMvc.perform(get("/test/controller"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.error").value("INVALID_REQUEST"))
			.andExpect(jsonPath("$.message").value("Controller exception occurred"));
	}

	@Test
	@DisplayName("서비스 예외 발생 시 올바른 에러 응답 반환")
	public void whenServiceExceptionThrown_thenReturnsProperErrorResponse() throws Exception {
		mockMvc.perform(get("/test/service"))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.error").value("RESOURCE_NOT_FOUND"))
			.andExpect(jsonPath("$.message").value("Service exception occurred"));
	}

	@Test
	@DisplayName("레포지토리 예외 발생 시 올바른 에러 응답 반환")
	public void whenRepositoryExceptionThrown_thenReturnsProperErrorResponse() throws Exception {
		mockMvc.perform(get("/test/repository"))
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("$.error").value("INTERNAL_SERVER_ERROR"))
			.andExpect(jsonPath("$.message").value("Repository exception occurred"));
	}
}
