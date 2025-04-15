package com.readup.server.book.domain.repository;

import com.readup.server.book.domain.Book;

public interface BookRepository {
	Book save(Book newBook);

	boolean existsByIsbnOrTitle(String isbn, String title);

	Book getBookById(Long id);
}
