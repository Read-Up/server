package com.readup.server.book.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.readup.server.book.application.dto.UpdateChapterListRequest;
import com.readup.server.book.domain.Book;
import com.readup.server.book.domain.repository.BookRepository;
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

		private static Stream<org.junit.jupiter.params.provider.Arguments> invalidChapterListProvider() {
			return Stream.of(
				org.junit.jupiter.params.provider.Arguments.of(
					List.of(
						new UpdateChapterListRequest.UpdateChapterRequest(1, "Chapter 1"),
						new UpdateChapterListRequest.UpdateChapterRequest(2, "Chapter 2"),
						new UpdateChapterListRequest.UpdateChapterRequest(2, "Chapter 3")
					),
					"중복된 챕터 번호로 실패"
				),
				org.junit.jupiter.params.provider.Arguments.of(
					List.of(
						new UpdateChapterListRequest.UpdateChapterRequest(2, "Chapter 1"),
						new UpdateChapterListRequest.UpdateChapterRequest(3, "Chapter 2"),
						new UpdateChapterListRequest.UpdateChapterRequest(4, "Chapter 3")
					),
					"1부터 시작하지 않는 챕터 번호로 실패"
				),
				Arguments.of(
					List.of(
						new UpdateChapterListRequest.UpdateChapterRequest(1, "Chapter 1"),
						new UpdateChapterListRequest.UpdateChapterRequest(2, "Chapter 2"),
						new UpdateChapterListRequest.UpdateChapterRequest(4, "Chapter 3")
					),
					"연속되지 않은 챕터 번호로 실패"
				)
			);
		}

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
		void updateChapterListFailWhenDoesNotExistBookTest() {
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

		@ParameterizedTest(name = "{index} - {1}")
		@MethodSource("invalidChapterListProvider")
		@DisplayName("챕터 리스트 업데이트 실패 테스트 - 챕터 번호 유효성 실패")
		void updateChapterListFailTest(
			List<UpdateChapterListRequest.UpdateChapterRequest> chapterRequests,
			String description
		) {
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
			UpdateChapterListRequest request = new UpdateChapterListRequest(chapterRequests);

			given(bookRepository.getBookById(bookId)).willReturn(book);

			// when-then
			assertThrows(DomainException.class, () -> bookCommandService.updateChapterList(bookId, request));
		}
	}
}