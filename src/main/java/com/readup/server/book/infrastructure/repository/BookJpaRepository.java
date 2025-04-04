package com.readup.server.book.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readup.server.book.domain.Book;

public interface BookJpaRepository extends JpaRepository<Book, Long> {

	boolean existsByIsbnOrTitle(String isbn, String title);
}
