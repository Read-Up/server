package com.readup.server.user.presentation;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.readup.server.user.generator.RandomNicknameGenerator;

@AutoConfigureRestDocs
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(RestDocumentationExtension.class)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void getRandomNickname() throws Exception {
		// given
		final String randomNickname = "지적인독서가";
		final String uri = "/api/public/users/random-nickname";

		// mocking
		try (MockedStatic<RandomNicknameGenerator> mockedStatic = mockStatic(RandomNicknameGenerator.class)) {
			mockedStatic.when(RandomNicknameGenerator::generate).thenReturn(randomNickname);

			// when && then
			mockMvc.perform(get(uri))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data").value(randomNickname))

				// docs
				.andDo(document("user-get-random-nickname",
					responseFields(
						fieldWithPath("success").type(BOOLEAN).description("응답 성공 여부"),
						fieldWithPath("data").type(STRING).description("랜덤 생성된 닉네임"),
						fieldWithPath("message").type(STRING).description("성공 메시지")
					)
				));
		}
	}
}