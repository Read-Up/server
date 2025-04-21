package com.readup.server.book.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.readup.server.book.domain.Book;
import com.readup.server.book.presentation.dto.GetBookResponse;

public interface BookRepository {
	Book save(Book newBook);

	boolean existsByIsbnOrTitle(String isbn, String title);

	Book getBookById(Long id);

	Page<GetBookResponse> searchBook(String title, String isbn, Pageable pageable);
}
