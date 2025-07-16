package com.readup.server.bookregistration.presentation;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static com.readup.server.bookregistration.domain.model.RegistrationStatus.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.readup.server.AbstractWebMvcTest;
import com.readup.server.bookregistration.application.BookRegistrationService;
import com.readup.server.bookregistration.domain.model.RegistrationStatus;
import com.readup.server.bookregistration.presentation.dto.CreateBookRegistrationRequest;
import com.readup.server.bookregistration.presentation.dto.CreateBookRegistrationResponse;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = BookRegistrationController.class, excludeFilters = @Filter(
	type = FilterType.REGEX,
	pattern = "com.readup.server.common..*")
)
class BookRegistrationControllerTest extends AbstractWebMvcTest {

	@MockitoBean
	private BookRegistrationService bookRegistrationService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("책 등록 요청")
	void CreateBookRegistration() throws Exception {
		// given
		final Long userId = 1L;
		final String title = "함께 자라기";
		final String isbn = "9788966262335";
		final String uri = "/api/private/book-registrations";
		final CreateBookRegistrationRequest request = new CreateBookRegistrationRequest(title, isbn);

		final Long bookRegistrationId = 1L;
		final RegistrationStatus bookRegistrationStatus = PENDING;
		final CreateBookRegistrationResponse response = new CreateBookRegistrationResponse(bookRegistrationId, userId,
			title, isbn, bookRegistrationStatus);

		// mocking
		SecurityContextHolder.getContext().setAuthentication(mockAuthentication(userId));

		when(bookRegistrationService.createBookRegistration(userId, request))
			.thenReturn(response);

		// when && then
		mockMvc.perform(post(uri)
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpectAll(
				status().isOk(),
				jsonPath("$.success").value(true),
				jsonPath("$.data.requesterId").value(userId),
				jsonPath("$.data.title").value(title),
				jsonPath("$.data.isbn").value(isbn),
				jsonPath("$.data.registrationStatus").value(bookRegistrationStatus.name()))

			// docs
			.andDo(document("book-registration-create",
				responseFields(
					fieldWithPath("success").type(BOOLEAN).description("응답 성공 여부"),
					fieldWithPath("data.id").type(NUMBER).description("책 등록 요청 PK"),
					fieldWithPath("data.requesterId").type(NUMBER).description("책 등록 요청 유저 PK"),
					fieldWithPath("data.title").type(STRING).description("책 제목"),
					fieldWithPath("data.isbn").type(STRING).description("책 ISBN"),
					fieldWithPath("data.registrationStatus").type(STRING).description("책 등록 요청 상태"),
					fieldWithPath("message").type(STRING).description("성공 메시지"))));
	}

	private Authentication mockAuthentication(Long userId) {
		return new UsernamePasswordAuthenticationToken(userId, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
	}
}