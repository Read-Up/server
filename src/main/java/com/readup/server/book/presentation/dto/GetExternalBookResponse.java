package com.readup.server.book.presentation.dto;

import java.util.List;

import com.readup.server.book.domain.Book;
import com.readup.server.book.domain.Chapter;

import lombok.Builder;

@Builder
public record GetExternalBookResponse(
	Long bookId,
	String bookTitle,
	String publisher,
	String author,
	String isbn,
	List<String> tableOfContents
) {
	public static GetExternalBookResponse from(Book book) {
		return GetExternalBookResponse.builder()
			.bookId(book.getId())
			.bookTitle(book.getTitle())
			.publisher(book.getPublisher())
			.author(book.getAuthor())
			.isbn(book.getIsbn())
			.tableOfContents(book.getChapterList().stream().map(Chapter::getName).toList())
			.build();
	}
}
