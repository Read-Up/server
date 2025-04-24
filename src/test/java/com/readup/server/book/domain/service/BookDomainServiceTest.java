package com.readup.server.book.domain.service;

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

import com.readup.server.book.domain.Book;
import com.readup.server.book.domain.repository.BookRepository;
import com.readup.server.common.exception.DomainException;

@ExtendWith(MockitoExtension.class)
class BookDomainServiceTest {

	@InjectMocks
	private BookDomainService bookDomainService;

	@Mock
	private BookRepository bookRepository;

	@Nested
	@DisplayName("책 저장 테스트")
	class saveTest {

		@Test
		@DisplayName("책 저장 성공 테스트")
		void saveSuccessTest() {
			// given
			Book book = Book.builder()
				.title("Test Book")
				.publisher("Test Publisher")
				.author("Test Author")
				.isbn("1234567890123")
				.titleUrl("http://example.com/test.jpg")
				.chapterList(List.of())
				.build();

			Book respositorySavedBook = Book.builder()
				.id(1L)
				.title("Test Book")
				.publisher("Test Publisher")
				.author("Test Author")
				.isbn("1234567890123")
				.titleUrl("http://example.com/test.jpg")
				.chapterList(List.of())
				.build();

			given(bookRepository.save(any(Book.class))).willReturn(respositorySavedBook);

			// when
			Book domainServiceSavedBook = bookDomainService.save(book);

			// then
			verify(bookRepository, times(1)).save(any());
			assertEquals(1L, domainServiceSavedBook.getId());
			assertEquals(book.getTitle(), domainServiceSavedBook.getTitle());
			assertEquals(book.getPublisher(), domainServiceSavedBook.getPublisher());
			assertEquals(book.getAuthor(), domainServiceSavedBook.getAuthor());
			assertEquals(book.getIsbn(), domainServiceSavedBook.getIsbn());
			assertEquals(book.getTitleUrl(), domainServiceSavedBook.getTitleUrl());
			assertEquals(book.getChapterList(), domainServiceSavedBook.getChapterList());
		}
	}

	@Nested
	@DisplayName("책 ISBN으로 존재하지 않는지 확인하는 테스트")
	class doesNotExistBookByIsbnTest {

		@Test
		void doesNotExistBookByIsbnSuccessTest() {

			// given
			String isbn = "1234567890";
			given(bookRepository.existsByIsbn(anyString())).willReturn(false);

			// when
			bookDomainService.doesNotExistBookByIsbn(isbn);

			// then
			verify(bookRepository, times(1)).existsByIsbn(isbn);
		}

		@Test
		void doesNotExistBookByIsbnFailTest() {

			// given
			String isbn = "1234567890";
			given(bookRepository.existsByIsbn(anyString())).willReturn(true);

			// when & then
			assertThrows(DomainException.class, () -> bookDomainService.doesNotExistBookByIsbn(isbn));
			verify(bookRepository, times(1)).existsByIsbn(isbn);
		}
	}
}