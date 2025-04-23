package com.readup.server.book.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readup.server.book.domain.Book;
import com.readup.server.book.domain.repository.BookRepository;
import com.readup.server.common.exception.DomainException;
import com.readup.server.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookDomainService {

	private final BookRepository bookRepository;

	public void doesNotExistBookByIsbn(String isbn) {
		if (bookRepository.existsByIsbn(isbn)) {
			throw new DomainException(ErrorCode.DUPLICATE_BOOK);
		}
	}

	@Transactional
	public Book save(Book book) {
		return bookRepository.save(book);
	}
}
