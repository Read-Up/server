package com.readup.server.book.application;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import com.readup.server.book.domain.repository.BookRepository;
import com.readup.server.book.presentation.dto.GetBookResponse;
import com.readup.server.book.presentation.dto.SearchBookRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookQueryService {

	private final BookRepository bookRepository;

	public PagedModel<GetBookResponse> searchBook(SearchBookRequest searchBookRequest, Pageable pageable) {
		return new PagedModel<>(
			bookRepository.searchBook(searchBookRequest.title(), searchBookRequest.isbn(), pageable));
	}
}
