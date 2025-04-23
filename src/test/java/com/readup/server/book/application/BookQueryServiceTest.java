package com.readup.server.book.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import com.readup.server.book.application.dto.GetBookResponse;
import com.readup.server.book.application.dto.SearchBookRequest;
import com.readup.server.book.domain.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
class BookQueryServiceTest {

	@InjectMocks
	private BookQueryService bookQueryService;

	@Mock
	private BookRepository bookRepository;

	@Nested
	@DisplayName("책 검색 테스트")
	class SearchBookTest {

		@Test
		@DisplayName("책 검색 성공")
		void searchBookSuccessTest() {
			// given
			String title = "testTitle";
			String isbn = "1234567890";
			SearchBookRequest searchBookRequest = new SearchBookRequest(title, isbn);
			GetBookResponse getBookResponse = GetBookResponse.builder()
				.bookId(1L)
				.title(title)
				.author("testAuthor")
				.publisher("testPublisher")
				.titleUrl("http://test.com")
				.build();

			given(bookRepository.searchBook(title, isbn, Pageable.unpaged()))
				.willReturn(new PageImpl<>(List.of(getBookResponse)));

			// when
			PagedModel<GetBookResponse> result = bookQueryService.searchBook(searchBookRequest, Pageable.unpaged());

			// then
			assertNotNull(result);
			assertEquals(getBookResponse.author(), result.getContent().getFirst().author());
			assertEquals(getBookResponse.publisher(), result.getContent().getFirst().publisher());
			assertEquals(getBookResponse.title(), result.getContent().getFirst().title());
			assertEquals(getBookResponse.titleUrl(), result.getContent().getFirst().titleUrl());
			assertEquals(getBookResponse.bookId(), result.getContent().getFirst().bookId());
			assertEquals(1, result.getContent().size());
		}
	}

}