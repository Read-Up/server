package com.readup.server.book.application;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readup.server.book.application.dto.GetBookResponse;
import com.readup.server.book.application.dto.SearchBookRequest;
import com.readup.server.book.application.dto.SearchBookResponse;
import com.readup.server.book.domain.Book;
import com.readup.server.book.domain.repository.BookRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookQueryService {

	private final BookRepository bookRepository;

	@Transactional(readOnly = true)
	public PagedModel<SearchBookResponse> searchBook(SearchBookRequest searchBookRequest, Pageable pageable) {
		return new PagedModel<>(
			bookRepository.searchBook(searchBookRequest.title(), searchBookRequest.isbn(), pageable));
	}

	@Transactional(readOnly = true)
	public GetBookResponse getBookById(Long bookId) {
		Book book = bookRepository.getBookById(bookId);

		return GetBookResponse.from(book);
	}
}
