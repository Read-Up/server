package com.readup.server.book.application.client.vo;

import com.readup.server.book.domain.Book;

import lombok.Builder;

@Builder
public record BookInfoVO(
	String bookTitle,
	String publisher,
	String author,
	String isbn,
	String titleUrl,
	String summary,
	String rawChapter
) {
	public Book toEntity() {
		return Book.builder()
			.title(bookTitle)
			.author(author)
			.publisher(publisher)
			.isbn(isbn)
			.titleUrl(titleUrl)
			.build();
	}
}
