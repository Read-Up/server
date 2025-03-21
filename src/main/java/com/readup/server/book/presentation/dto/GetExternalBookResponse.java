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
	public static GetExternalBookResponse from(Book savedBook) {
		return GetExternalBookResponse.builder()
			.bookId(savedBook.getId())
			.bookTitle(savedBook.getTitle())
			.publisher(savedBook.getPublisher())
			.author(savedBook.getAuthor())
			.isbn(savedBook.getIsbn())
			.tableOfContents(savedBook.getChapterList().stream().map(Chapter::getName).toList())
			.build();
	}
}
