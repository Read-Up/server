package com.readup.server.book.presentation;

import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Map;

import com.readup.server.common.config.WebConfig;
import com.readup.server.common.resolver.CurrentAuthUserArgumentResolver;
import com.readup.server.common.resolver.CurrentSocialAccountArgumentResolver;
import com.readup.server.common.resolver.CurrentUserArgumentResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.MultiValueMap;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.readup.server.AbstractWebMvcTest;
import com.readup.server.book.application.BookCommandService;
import com.readup.server.book.application.BookQueryService;
import com.readup.server.book.application.dto.GetBookResponse;
import com.readup.server.book.application.dto.SearchBookRequest;
import com.readup.server.book.application.dto.SearchBookResponse;
import com.readup.server.book.application.dto.UpdateChapterListRequest;
import com.readup.server.book.domain.Book;
import com.readup.server.book.domain.Chapter;
import com.readup.server.common.dto.ApiResponse;
import com.readup.server.common.exception.DomainException;
import com.readup.server.common.exception.ErrorCode;

@WebMvcTest(controllers = BookController.class,
		excludeFilters = {
				@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
						WebConfig.class,
						CurrentUserArgumentResolver.class,
						CurrentAuthUserArgumentResolver.class,
						CurrentSocialAccountArgumentResolver.class
				})
		}
)
@AutoConfigureMockMvc(addFilters = false)
class BookControllerTest extends AbstractWebMvcTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private BookCommandService bookCommandService;

	@MockitoBean
	private BookQueryService bookQueryService;

	private ObjectMapper objectMapper;

	@BeforeEach
	public void setUp() {
		objectMapper = new ObjectMapper();
	}

	@Nested
	@DisplayName("책 검색 테스트")
	class SearchBookTest {
		private final String uri = "/public/books";

		@Test
		@DisplayName("책 이름 기반 검색 성공 테스트")
		void searchBookByNameSuccessTest() throws Exception {
			// given
			MultiValueMap<String, String> queryParams =
				MultiValueMap.fromSingleValue(Map.of("title", "토비"));

			PagedModel<SearchBookResponse> pagedModel = new PagedModel<>(
				new PageImpl<>(List.of(SearchBookResponse.builder()
					.bookId(1L)
					.title("토비의 스프링 3.1 Vol. 1 스프링의 이해와 원리")
					.author("이일민")
					.publisher("에이콘출판(주)")
					.isbn("9788960773417")
					.titleUrl("http://www.nl.go.kr/seoji/fu/ecip/dbfiles/CIP_FILES_TBL/2577606_3.jpg")
					.build()),
					Pageable.ofSize(10),
					1
				));

			given(bookQueryService.searchBook(any(SearchBookRequest.class), any(Pageable.class)))
				.willReturn(pagedModel);

			// when
			ResultActions resultActions = mockMvc.perform(get(uri)
				.params(queryParams)
				.contentType(MediaType.APPLICATION_JSON));

			// then
			resultActions.andExpectAll(
				status().isOk(),
				jsonPath("$.success").value(true),
				jsonPath("$.data.content[0].bookId").value(1L),
				jsonPath("$.data.content[0].title").value("토비의 스프링 3.1 Vol. 1 스프링의 이해와 원리"),
				jsonPath("$.data.content[0].author").value("이일민"),
				jsonPath("$.data.content[0].publisher").value("에이콘출판(주)"),
				jsonPath("$.data.content[0].isbn").value("9788960773417"),
				jsonPath("$.data.content[0].titleUrl").value(
					"http://www.nl.go.kr/seoji/fu/ecip/dbfiles/CIP_FILES_TBL/2577606_3.jpg"),
				jsonPath("$.data.page.size").value(10),
				jsonPath("$.data.page.number").value(0),
				jsonPath("$.data.page.totalElements").value(1),
				jsonPath("$.data.page.totalPages").value(1),
				jsonPath("$.message").value(ApiResponse.DEFAULT_SUCCESS_MESSAGE)
			);

			//docs
			resultActions.andDo(
				MockMvcRestDocumentationWrapper.document("책 이름 기반 검색 성공",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					resource(ResourceSnippetParameters.builder()
						.tag("Book")
						.queryParameters(
							parameterWithName("title").optional().description("책 제목"),
							parameterWithName("isbn").optional().description("책 ISBN"),
							parameterWithName("page").optional().description("페이지 번호"),
							parameterWithName("size").optional().description("페이지 사이즈")
						)
						.responseFields(
							fieldWithPath("success").description("성공 여부"),
							fieldWithPath("message").description("메시지"),
							fieldWithPath("data.content[0].bookId").description("책 ID"),
							fieldWithPath("data.content[0].title").description("책 제목"),
							fieldWithPath("data.content[0].author").description("책 저자"),
							fieldWithPath("data.content[0].publisher").description("책 출판사"),
							fieldWithPath("data.content[0].isbn").description("책 ISBN"),
							fieldWithPath("data.content[0].titleUrl").description("책 표지 URL"),
							fieldWithPath("data.page.size").description("페이지 사이즈"),
							fieldWithPath("data.page.number").description("현재 페이지 번호"),
							fieldWithPath("data.page.totalElements").description("총 요소 수"),
							fieldWithPath("data.page.totalPages").description("총 페이지 수")
						)
						.build()
					)
				)
			);
		}
	}

	@Nested
	@DisplayName("책 상세 정보 조회 테스트")
	class getBookByBookId {
		private final String uri = "/public/books/{bookId}";
		private final Long bookId = 1L;

		@Test
		@DisplayName("책 상세 정보 조회 성공 테스트")
		void getBookByBookIdSuccessTest() throws Exception {
			//given
			Book book = Book.builder()
				.id(1L)
				.isbn("9788960773417")
				.title("토비의 스프링 3.1 Vol. 1 스프링의 이해와 원리")
				.author("이일민")
				.publisher("에이콘출판(주)")
				.titleUrl("http://www.nl.go.kr/seoji/fu/ecip/dbfiles/CIP_FILES_TBL/2577606_3.jpg")
				.chapterList(List.of(
					Chapter.builder()
						.id(1L)
						.chapterOrder(1)
						.name("1장 소개")
						.build(),
					Chapter.builder()
						.id(2L)
						.chapterOrder(2)
						.name("2장 설치와 설정")
						.build(),
					Chapter.builder()
						.id(3L)
						.chapterOrder(3)
						.name("3장 스프링의 이해")
						.build()
				))
				.build();
			given(bookQueryService.getBookById(any(Long.class)))
				.willReturn(GetBookResponse.from(book));

			//when
			ResultActions resultActions = mockMvc.perform(get(uri, bookId)
				.contentType(MediaType.APPLICATION_JSON));

			//then
			resultActions.andExpectAll(
				status().isOk(),
				jsonPath("$.success").value(true),
				jsonPath("$.data.bookId").value(1L),
				jsonPath("$.data.title").value("토비의 스프링 3.1 Vol. 1 스프링의 이해와 원리"),
				jsonPath("$.data.author").value("이일민"),
				jsonPath("$.data.publisher").value("에이콘출판(주)"),
				jsonPath("$.data.isbn").value("9788960773417"),
				jsonPath("$.data.titleUrl").value(
					"http://www.nl.go.kr/seoji/fu/ecip/dbfiles/CIP_FILES_TBL/2577606_3.jpg"),
				jsonPath("$.data.chapterList[0].chapterId").value(1L),
				jsonPath("$.data.chapterList[0].chapterOrder").value(1),
				jsonPath("$.data.chapterList[0].chapterName").value("1장 소개"),
				jsonPath("$.data.chapterList[1].chapterId").value(2L),
				jsonPath("$.data.chapterList[1].chapterOrder").value(2),
				jsonPath("$.data.chapterList[1].chapterName").value("2장 설치와 설정"),
				jsonPath("$.data.chapterList[2].chapterId").value(3L),
				jsonPath("$.data.chapterList[2].chapterOrder").value(3),
				jsonPath("$.data.chapterList[2].chapterName").value("3장 스프링의 이해"),
				jsonPath("$.message").value(ApiResponse.DEFAULT_SUCCESS_MESSAGE)
			);

			//docs
			resultActions.andDo(
				MockMvcRestDocumentationWrapper.document("책 상세 조회 성공",
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
		@DisplayName("책 상세 정보 조회 실패 테스트 - 존재하지 않는 책")
		void getBookByBookIdFailWhenBookNotExistTest() throws Exception {
			//given
			given(bookQueryService.getBookById(any(Long.class)))
				.willThrow(new DomainException(ErrorCode.BOOK_NOT_FOUND));

			//when
			ResultActions resultActions = mockMvc.perform(get(uri, bookId)
				.contentType(MediaType.APPLICATION_JSON));

			//then
			resultActions.andExpectAll(
				status().isNotFound(),
				jsonPath("$.error").value(ErrorCode.BOOK_NOT_FOUND.name()),
				jsonPath("$.message").value(ErrorCode.BOOK_NOT_FOUND.getMessage())
			);

			//docs
			resultActions.andDo(
				MockMvcRestDocumentationWrapper.document("책 상세 조회 실패 - 존재하지 않는 책",
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

	@Nested
	@DisplayName("책 챕터 업데이트 테스트")
	class UpdateChapterList {
		private final String uri = "/public/books/{bookId}/chapters";
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