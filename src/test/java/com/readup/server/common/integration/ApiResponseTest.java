package com.readup.server.common.integration;

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
public class ApiResponseTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("정상 응답 반환 테스트")
	public void whenNormalEndpointCalled_thenReturnsSuccessResponse() throws Exception {
		mockMvc.perform(get("/test/success"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.data").value("Success Response Data"))
			.andExpect(jsonPath("$.message").value("정상 응답입니다."));
	}
}
