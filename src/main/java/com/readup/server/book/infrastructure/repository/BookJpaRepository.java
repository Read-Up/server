package com.readup.server.book.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readup.server.book.domain.Book;
import com.readup.server.book.domain.repository.BookRepository;
import com.readup.server.common.exception.ErrorCode;
import com.readup.server.common.exception.RepositoryException;

public interface BookJpaRepository extends JpaRepository<Book, Long>, BookRepository {

	boolean existsByIsbnOrTitle(String isbn, String title);

	Optional<Book> findBookById(Long id);

	default Book getBookById(Long id) {
		return findBookById(id).orElseThrow(() -> new RepositoryException(ErrorCode.BOOK_NOT_FOUND));
	}
}
