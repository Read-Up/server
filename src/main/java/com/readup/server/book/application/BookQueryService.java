package com.readup.server.book.application;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import com.readup.server.book.application.dto.SearchBookRequest;
import com.readup.server.book.application.dto.SearchBookResponse;
import com.readup.server.book.domain.repository.BookRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookQueryService {

	private final BookRepository bookRepository;

	public PagedModel<SearchBookResponse> searchBook(SearchBookRequest searchBookRequest, Pageable pageable) {
		return new PagedModel<>(
			bookRepository.searchBook(searchBookRequest.title(), searchBookRequest.isbn(), pageable));
	}
}
