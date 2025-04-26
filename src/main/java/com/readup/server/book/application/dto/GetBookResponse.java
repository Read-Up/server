package com.readup.server.book.application.dto;

import com.readup.server.book.domain.Book;

import lombok.Builder;

@Builder
public record GetBookResponse(
	Long bookId,
	String title,
	String author,
	String publisher,
	String titleUrl
) {
	public static GetBookResponse from(
		Book book
	) {
		return GetBookResponse.builder()
			.bookId(book.getId())
			.title(book.getTitle())
			.author(book.getAuthor())
			.publisher(book.getPublisher())
			.titleUrl(book.getTitleUrl())
			.build();
	}
}
