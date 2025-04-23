package com.readup.server.book.application.dto;

import java.util.List;

import com.readup.server.book.domain.Book;
import com.readup.server.book.domain.Chapter;

import lombok.Builder;

@Builder
public record RetrieveBookResponse(
	Long bookId,
	String bookTitle,
	String publisher,
	String author,
	String isbn,
	List<String> tableOfContents
) {
	public static RetrieveBookResponse from(Book book) {
		return RetrieveBookResponse.builder()
			.bookId(book.getId())
			.bookTitle(book.getTitle())
			.publisher(book.getPublisher())
			.author(book.getAuthor())
			.isbn(book.getIsbn())
			.tableOfContents(book.getChapterList().stream().map(Chapter::getName).toList())
			.build();
	}
}
