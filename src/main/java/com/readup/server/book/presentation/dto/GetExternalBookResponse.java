package com.readup.server.book.presentation.dto;

import java.util.List;

public record GetExternalBookResponse(
	Long bookId,
	String bookTitle,
	String publisher,
	String author,
	String isbn,
	List<String> tableOfContents
) {
}
