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
import com.readup.server.book.application.dto.SearchBookResponse;
import com.readup.server.book.domain.Book;
import com.readup.server.book.domain.Chapter;
import com.readup.server.book.domain.repository.BookRepository;
import com.readup.server.common.exception.ErrorCode;
import com.readup.server.common.exception.RepositoryException;

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
			SearchBookResponse searchBookResponse = SearchBookResponse.builder()
				.bookId(1L)
				.title(title)
				.author("testAuthor")
				.publisher("testPublisher")
				.titleUrl("http://test.com")
				.build();

			given(bookRepository.searchBook(title, isbn, Pageable.unpaged()))
				.willReturn(new PageImpl<>(List.of(searchBookResponse)));

			// when
			PagedModel<SearchBookResponse> result = bookQueryService.searchBook(searchBookRequest, Pageable.unpaged());

			// then
			assertNotNull(result);
			assertEquals(searchBookResponse.author(), result.getContent().getFirst().author());
			assertEquals(searchBookResponse.publisher(), result.getContent().getFirst().publisher());
			assertEquals(searchBookResponse.title(), result.getContent().getFirst().title());
			assertEquals(searchBookResponse.titleUrl(), result.getContent().getFirst().titleUrl());
			assertEquals(searchBookResponse.bookId(), result.getContent().getFirst().bookId());
			assertEquals(1, result.getContent().size());
		}
	}

	@Nested
	@DisplayName("책 상세 조회 테스트")
	class GetBookByBookIdTest {

		@Test
		@DisplayName("책 상세 조회 성공")
		void getBookByBookIdSuccessTest() {
			Long bookId = 1L;
			// given
			Book book = Book.builder()
				.id(bookId)
				.title("testTitle")
				.author("testAuthor")
				.publisher("testPublisher")
				.isbn("1234567890")
				.titleUrl("http://test.com")
				.chapterList(List.of(
					Chapter.builder()
						.id(1L)
						.chapterOrder(1)
						.name("testChapter1")
						.build(),
					Chapter.builder()
						.id(2L)
						.chapterOrder(2)
						.name("testChapter2")
						.build(),
					Chapter.builder()
						.id(3L)
						.chapterOrder(3)
						.name("testChapter3")
						.build()
				))
				.build();

			given(bookRepository.getBookById(anyLong())).willReturn(book);

			// when
			GetBookResponse result = bookQueryService.getBookById(bookId);

			// then
			assertNotNull(result);
			assertEquals(bookId, result.bookId());
			assertEquals(book.getTitle(), result.title());
			assertEquals(book.getAuthor(), result.author());
			assertEquals(book.getPublisher(), result.publisher());
			assertEquals(book.getIsbn(), result.isbn());
			assertEquals(book.getTitleUrl(), result.titleUrl());
			assertEquals(book.getChapterList().size(), result.chapterList().size());
			for (int i = 0; i < book.getChapterList().size(); i++) {
				assertEquals(
					book.getChapterList().get(i).getId(),
					result.chapterList().get(i).chapterId()
				);
				assertEquals(
					book.getChapterList().get(i).getChapterOrder(),
					result.chapterList().get(i).chapterOrder()
				);
				assertEquals(
					book.getChapterList().get(i).getName(),
					result.chapterList().get(i).chapterName()
				);
			}
		}

		@Test
		@DisplayName("책 상세 조회 실패 - 존재하지 않는 책 ID로 조회")
		void getBookByBookIdFailWhenBookNotFound() {
			// given
			Long bookId = 1L;

			given(bookRepository.getBookById(anyLong())).willThrow(new RepositoryException(ErrorCode.BOOK_NOT_FOUND));

			// when-then
			RepositoryException exception = assertThrows(RepositoryException.class,
				() -> bookQueryService.getBookById(bookId));
			assertEquals(ErrorCode.BOOK_NOT_FOUND, exception.getErrorCode());
		}
	}
}