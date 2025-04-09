package com.readup.server.book.presentation;

import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.readup.server.book.application.BookCommandService;
import com.readup.server.book.presentation.dto.UpdateChapterListRequest;
import com.readup.server.common.dto.ApiResponse;
import com.readup.server.common.exception.DomainException;
import com.readup.server.common.exception.ErrorCode;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
class BookControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private BookCommandService bookCommandService;

	private ObjectMapper objectMapper;

	@BeforeEach
	public void setUp() {
		objectMapper = new ObjectMapper();
	}

	@Nested
	@DisplayName("책 챕터 업데이트 테스트")
	class UpdateChapterList {
		private final String uri = "/api/public/books/{bookId}/chapters";
		private final Long bookId = 1L;
		private final UpdateChapterListRequest updateChapterListRequest = new UpdateChapterListRequest(List.of(
			new UpdateChapterListRequest.UpdateChapterRequest(1, "Chapter 1"),
			new UpdateChapterListRequest.UpdateChapterRequest(2, "Chapter 2"),
			new UpdateChapterListRequest.UpdateChapterRequest(3, "Chapter 3")
		));

		@Test
		@DisplayName("책 챕터 업데이트 성공 테스트")
		void updateChapterListSuccessTest() throws Exception {
			// given
			willDoNothing().given(bookCommandService)
				.updateChapterList(any(Long.class), any(UpdateChapterListRequest.class));

			// when
			ResultActions resultActions = mockMvc.perform(put(uri, bookId)
				.content(objectMapper.writeValueAsString(updateChapterListRequest))
				.contentType(MediaType.APPLICATION_JSON));

			// then
			resultActions.andExpectAll(
				status().isOk(),
				jsonPath("$.success").value(true),
				jsonPath("$.message").value(ApiResponse.DEFAULT_SUCCESS_MESSAGE)
			);

			//docs
			resultActions.andDo(
				MockMvcRestDocumentationWrapper.document("챕터 리스트 수정 성공",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					resource(ResourceSnippetParameters.builder()
						.tag("Book")
						.pathParameters(
							parameterWithName("bookId").type(SimpleType.NUMBER).description("책 ID")
						)
						.build()
					)
				)
			);
		}

		@Test
		@DisplayName("책 챕터 업데이트 실패 테스트 - 존재하지 않는 책")
		void updateChapterListFailTest() throws Exception {
			// given
			willThrow(new DomainException(ErrorCode.BOOK_NOT_FOUND)).given(bookCommandService)
				.updateChapterList(any(Long.class), any(UpdateChapterListRequest.class));

			// when
			ResultActions resultActions = mockMvc.perform(put(uri, bookId)
				.content(objectMapper.writeValueAsString(updateChapterListRequest))
				.contentType(MediaType.APPLICATION_JSON));

			// then
			resultActions.andExpectAll(
				status().isNotFound(),
				jsonPath("$.error").value(ErrorCode.BOOK_NOT_FOUND.name()),
				jsonPath("$.message").value(ErrorCode.BOOK_NOT_FOUND.getMessage())
			);

			//docs
			resultActions.andDo(
				MockMvcRestDocumentationWrapper.document("챕터 리스트 수정 실패 - 존재하지 않는 책",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					resource(ResourceSnippetParameters.builder()
						.tag("Book")
						.pathParameters(
							parameterWithName("bookId").type(SimpleType.NUMBER).description("책 ID")
						)
						.build()
					)
				)
			);
		}
	}
}