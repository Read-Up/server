package com.readup.server.book.presentation;

import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.readup.server.book.application.BookInfoService;
import com.readup.server.book.presentation.dto.GetExternalBookResponse;
import com.readup.server.common.dto.ApiResponse;
import com.readup.server.common.exception.ErrorCode;
import com.readup.server.common.exception.ServiceException;

@WebMvcTest(BookInfoController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
class BookInfoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private BookInfoService bookInfoService;

	@Nested
	@DisplayName("ISBN 기반 책 정보 가져오기 API 테스트")
	class GetBookInfo {

		private final Long isbn = 9788960773417L;
		private final String uri = "/public/external-books/{isbn}";

		@Test
		@DisplayName("ISBN 기반 책 정보 가져오기 성공")
		void getBookInfoSuccess() throws Exception {
			// given
			GetExternalBookResponse getExternalBookResponse = GetExternalBookResponse.builder()
				.bookTitle("토비의 스프링 3.1 Vol. 1 스프링의 이해와 원리")
				.publisher("에이콘출판(주)")
				.author("이일민")
				.isbn(String.valueOf(isbn))
				.build();

			given(bookInfoService.getBookInfo(anyString())).willReturn(getExternalBookResponse);

			// when
			ResultActions resultActions = mockMvc.perform(get(uri, isbn));

			// then
			resultActions.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.bookTitle").value(getExternalBookResponse.bookTitle()))
				.andExpect(jsonPath("$.data.publisher").value(getExternalBookResponse.publisher()))
				.andExpect(jsonPath("$.data.author").value(getExternalBookResponse.author()))
				.andExpect(jsonPath("$.data.isbn").value(getExternalBookResponse.isbn()))
				.andExpect(jsonPath("$.message").value(ApiResponse.DEFAULT_SUCCESS_MESSAGE));

			// docs
			resultActions.andDo(
				MockMvcRestDocumentationWrapper.document(
					"ISBN 기반 책 정보 조회 성공",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					resource(ResourceSnippetParameters.builder()
						.tag("BookInfo")
						.pathParameters(
							parameterWithName("isbn").type(SimpleType.NUMBER).description("책 ISBN")
						)
						.build()
					)
				)
			);
		}

		@Test
		@DisplayName("ISBN 기반 책 정보 가져오기 실패 - 이미 존재하는 책")
		void getBookInfoFailWhenBookAlreadyExists() throws Exception {
			// given
			given(bookInfoService.getBookInfo(anyString()))
				.willThrow(new ServiceException(ErrorCode.DUPLICATE_BOOK, "Book already exists"));

			// when
			ResultActions resultActions = mockMvc.perform(get(uri, isbn));

			// then
			resultActions.andExpect(status().isConflict())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.status").value(409))
				.andExpect(jsonPath("$.error").value(ErrorCode.DUPLICATE_BOOK.name()))
				.andExpect(jsonPath("$.message").value("Book already exists"));

			resultActions.andDo(
				MockMvcRestDocumentationWrapper.document(
					"ISBN 기반 책 정보 조회 실패 - 이미 존재하는 책",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					resource(ResourceSnippetParameters.builder()
						.tag("BookInfo")
						.pathParameters(
							parameterWithName("isbn").type(SimpleType.NUMBER).description("책 ISBN")
						)
						.build()
					)
				)
			);
		}

		@Test
		@DisplayName("ISBN 기반 책 정보 가져오기 실패 - 존재하지않는 ISBN")
		void getBookInfoFailWhenBookNotFound() throws Exception {
			// given
			given(bookInfoService.getBookInfo(anyString()))
				.willThrow(new ServiceException(ErrorCode.BOOK_NOT_FOUND));

			// when
			ResultActions resultActions = mockMvc.perform(get(uri, isbn));

			// then
			resultActions.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.error").value(ErrorCode.BOOK_NOT_FOUND.name()))
				.andExpect(jsonPath("$.message").value(ErrorCode.BOOK_NOT_FOUND.getMessage()));

			resultActions.andDo(
				MockMvcRestDocumentationWrapper.document(
					"ISBN 기반 책 정보 조회 실패 - 존재하지 않는 ISBN",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					resource(ResourceSnippetParameters.builder()
						.tag("BookInfo")
						.pathParameters(
							parameterWithName("isbn").type(SimpleType.NUMBER).description("책 ISBN")
						)
						.build()
					)
				)
			);
		}
	}
}