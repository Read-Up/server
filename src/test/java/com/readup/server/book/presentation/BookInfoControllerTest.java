package com.readup.server.book.presentation;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.readup.server.book.application.BookInfoService;
import com.readup.server.book.presentation.dto.GetExternalBookResponse;
import com.readup.server.common.security.SecurityConfig;

@WebMvcTest(BookInfoController.class)
@Import(SecurityConfig.class)
class BookInfoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private BookInfoService bookInfoService;

	@Nested
	@DisplayName("ISBN 기반 책 정보 가져오기 API 테스트")
	class GetBookInfo {

		private final String isbn = "9788960773417";
		private final String uri = "/external-books/" + isbn;

		@Test
		@DisplayName("ISBN 기반 책 정보 가져오기 성공")
		void getBookInfoSuccess() throws Exception {
			// given
			GetExternalBookResponse getExternalBookResponse = GetExternalBookResponse.builder()
				.bookTitle("토비의 스프링 3.1 Vol. 1 스프링의 이해와 원리")
				.publisher("에이콘출판(주)")
				.author("이일민")
				.isbn(isbn)
				.build();

			given(bookInfoService.getBookInfo(anyString())).willReturn(getExternalBookResponse);

			// when
			ResultActions resultActions = mockMvc.perform(get(uri));

			// then
			resultActions.andExpect(status().isOk())
				.andExpect(jsonPath("$.bookTitle").value(getExternalBookResponse.bookTitle()))
				.andExpect(jsonPath("$.publisher").value(getExternalBookResponse.publisher()))
				.andExpect(jsonPath("$.author").value(getExternalBookResponse.author()))
				.andExpect(jsonPath("$.isbn").value(getExternalBookResponse.isbn()));
		}
	}
}