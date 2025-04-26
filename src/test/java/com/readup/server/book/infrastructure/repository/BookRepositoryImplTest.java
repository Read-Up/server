package com.readup.server.book.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.readup.server.book.application.dto.GetBookResponse;
import com.readup.server.book.domain.Book;
import com.readup.server.common.config.QuerydslConfig;
import com.readup.server.common.exception.RepositoryException;

@ActiveProfiles("test")
@Sql(
	executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
	scripts = {"/sql/test-init-book.sql", "/sql/test-init-chapter.sql"}
)
@DataJpaTest
@Import(QuerydslConfig.class)
class BookRepositoryImplTest {

	@Autowired
	private BookJpaRepository bookJpaRepository;

	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	private BookRepositoryImpl bookRepository;

	@BeforeEach
	public void init() {
		if (bookRepository == null) {
			bookRepository = new BookRepositoryImpl(jpaQueryFactory, bookJpaRepository);
		}
	}

	private Book saveSampleBook(String title, String isbn) {
		Book book = Book.builder()
			.title(title)
			.author("Author")
			.publisher("Publisher")
			.isbn(isbn)
			.build();
		return bookRepository.save(book);
	}

	@Nested
	@DisplayName("save()")
	class Save {

		@Test
		void shouldSaveBookSuccessfully() {
			Book book = saveSampleBook("Test Title", "1234567890123");

			assertNotNull(book.getId());
			assertTrue(bookRepository.existsByIsbn("1234567890123"));
		}
	}

	@Nested
	@DisplayName("getBookById()")
	class GetBookById {

		@Test
		void shouldReturnBookWhenExists() {
			Book saved = saveSampleBook("Test Title", "1234567890123");
			Book found = bookRepository.getBookById(saved.getId());

			assertNotNull(found);
			assertEquals("Test Title", found.getTitle());
		}

		@Test
		void shouldThrowExceptionWhenBookNotFound() {
			assertThrows(RepositoryException.class, () -> bookRepository.getBookById(-1L));
		}
	}

	@Nested
	@DisplayName("existsByIsbn()")
	class ExistsByIsbn {

		@Test
		void shouldReturnTrueWhenBookExists() {
			saveSampleBook("Some Title", "1234567890123");
			assertTrue(bookRepository.existsByIsbn("1234567890123"));
		}

		@Test
		void shouldReturnFalseWhenBookDoesNotExist() {
			assertFalse(bookRepository.existsByIsbn("1234567890123"));
		}
	}

	@Nested
	@DisplayName("searchBook()")
	class SearchBook {

		@Test
		void shouldReturnPageWhenMatchingBooksExist() {
			saveSampleBook("Java Programming", "1234567890123");
			saveSampleBook("Spring Programming", "1234567890124");

			Page<GetBookResponse> result = bookRepository.searchBook("Programming", null, PageRequest.of(0, 10));
			assertFalse(result.getContent().isEmpty());
		}

		@Test
		void shouldReturnEmptyPageWhenNoMatch() {
			Page<GetBookResponse> result = bookRepository.searchBook("No Match", null, PageRequest.of(0, 10));
			assertTrue(result.getContent().isEmpty());
		}
	}
}