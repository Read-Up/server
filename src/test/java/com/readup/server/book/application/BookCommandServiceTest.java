package com.readup.server.book.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.readup.server.book.domain.Book;
import com.readup.server.book.domain.repository.BookRepository;
import com.readup.server.book.presentation.dto.UpdateChapterListRequest;
import com.readup.server.common.exception.DomainException;
import com.readup.server.common.exception.ErrorCode;

@ExtendWith(MockitoExtension.class)
class BookCommandServiceTest {

	@InjectMocks
	private BookCommandService bookCommandService;

	@Mock
	private BookRepository bookRepository;

	@Nested
	@DisplayName("챕터 리스트 업데이트 테스트")
	class UpdateChapterListTest {

		@Test
		@DisplayName("챕터 리스트 업데이트 성공 테스트")
		void updateChapterListSuccessTest() {
			// given
			Long bookId = 1L;
			Book book = Book.builder()
				.id(bookId)
				.title("Test Book")
				.author("Test Author")
				.publisher("Test Publisher")
				.isbn("1234567890")
				.chapterList(new ArrayList<>())
				.build();
			UpdateChapterListRequest updateChapterListRequest = new UpdateChapterListRequest(List.of(
				new UpdateChapterListRequest.UpdateChapterRequest(1, "Chapter 1"),
				new UpdateChapterListRequest.UpdateChapterRequest(2, "Chapter 2"),
				new UpdateChapterListRequest.UpdateChapterRequest(3, "Chapter 3")
			));

			given(bookRepository.getBookById(1L)).willReturn(book);

			// when
			bookCommandService.updateChapterList(bookId, updateChapterListRequest);

			// then
			assertTrue(true);
		}

		@Test
		@DisplayName("챕터 리스트 업데이트 실패 테스트 - 존재하지 않는 책")
		void updateChapterListFailTest() {
			// given
			Long bookId = 1L;
			UpdateChapterListRequest updateChapterListRequest = new UpdateChapterListRequest(List.of(
				new UpdateChapterListRequest.UpdateChapterRequest(1, "Chapter 1"),
				new UpdateChapterListRequest.UpdateChapterRequest(2, "Chapter 2"),
				new UpdateChapterListRequest.UpdateChapterRequest(3, "Chapter 3")
			));

			given(bookRepository.getBookById(1L)).willThrow(new DomainException(ErrorCode.BOOK_NOT_FOUND));

			// when
			assertThrows(DomainException.class, () -> {
				bookCommandService.updateChapterList(bookId, updateChapterListRequest);
			});
		}
	}
}