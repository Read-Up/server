package com.readup.server.book.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.readup.server.book.domain.Book;
import com.readup.server.common.exception.RepositoryException;

@ExtendWith(MockitoExtension.class)
class BookRepositoryImplTest {

	@InjectMocks
	private BookRepositoryImpl bookRepositoryImpl;

	@Mock
	private BookJpaRepository bookJpaRepository;

	@Mock
	private JPAQueryFactory jpaQueryFactory;

	@Nested
	@DisplayName("ISBN 또는 제목으로 책 존재 여부 확인 테스트")
	class ExistsByIsbnOrTitleTest {
		@Test
		void existsByIsbnOrTitleSuccessTest() {
			// given
			String isbn = "1234567890";
			String title = "Test Book";
			given(bookJpaRepository.existsByIsbnOrTitle(isbn, title)).willReturn(true);

			// when
			boolean result = bookRepositoryImpl.existsByIsbnOrTitle(isbn, title);

			// then
			assertTrue(result);
			verify(bookJpaRepository).existsByIsbnOrTitle(isbn, title);
		}

		@Test
		void existsByIsbnOrTitleFailTest() {
			// given
			String isbn = "1234567890";
			String title = "Test Book";
			given(bookJpaRepository.existsByIsbnOrTitle(isbn, title)).willReturn(false);

			// when
			boolean result = bookRepositoryImpl.existsByIsbnOrTitle(isbn, title);

			// then
			assertFalse(result);
			verify(bookJpaRepository).existsByIsbnOrTitle(isbn, title);
		}
	}

	@Nested
	@DisplayName("Id로 책 조회 테스트")
	class GetBookByIdTest {
		@Test
		void getBookByIdSuccessTest() {
			// given
			Long id = 1L;
			Book book = mock(Book.class);
			given(bookJpaRepository.findBookById(id)).willReturn(java.util.Optional.of(book));

			// when
			Book result = bookRepositoryImpl.getBookById(id);

			// then
			assertEquals(book, result);
			verify(bookJpaRepository).findBookById(id);
		}

		@Test
		void getBookByIdFailWhenNotFoundTest() {
			// given
			Long id = 1L;
			given(bookJpaRepository.findBookById(id)).willReturn(java.util.Optional.empty());

			// when & then
			assertThrows(RepositoryException.class, () -> bookRepositoryImpl.getBookById(id));
			verify(bookJpaRepository).findBookById(id);
		}
	}

	@Nested
	@DisplayName("저장 테스트")
	class SaveTest {
		@Test
		void saveBookSuccessTest() {
			// given
			Book book = mock(Book.class);
			given(bookJpaRepository.save(book)).willReturn(book);

			// when
			Book result = bookRepositoryImpl.save(book);

			// then
			assertEquals(book, result);
			verify(bookJpaRepository).save(book);
		}
	}
}