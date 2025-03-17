package com.readup.server.book.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.readup.server.book.application.BookInfoService;
import com.readup.server.book.presentation.dto.GetExternalBookResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BookInfoController {

	private final BookInfoService bookInfoService;

	@GetMapping("/external-books/{isbn}")
	public GetExternalBookResponse getBookInfo(@PathVariable String isbn) {

		GetExternalBookResponse response = bookInfoService.getBookInfo(isbn);

		return response;
	}
}
