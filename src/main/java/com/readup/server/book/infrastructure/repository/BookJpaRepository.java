package com.readup.server.book.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.readup.server.book.domain.Book;

public interface BookJpaRepository extends JpaRepository<Book, Long> {

	boolean existsByIsbn(String isbn);

	@Query("SELECT b FROM Book b LEFT JOIN FETCH b.chapterList c WHERE b.id = :id")
	Optional<Book> findBookById(Long id);
}
