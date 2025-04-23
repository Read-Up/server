package com.readup.server.book.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.readup.server.book.application.dto.GetBookResponse;
import com.readup.server.book.domain.Book;

public interface BookRepository {
	Book save(Book newBook);

	boolean existsByIsbn(String isbn);

	Book getBookById(Long id);

	Page<GetBookResponse> searchBook(String title, String isbn, Pageable pageable);
}
