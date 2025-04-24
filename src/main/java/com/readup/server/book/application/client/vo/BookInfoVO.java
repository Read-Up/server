package com.readup.server.book.application.client.vo;

import com.readup.server.book.domain.Book;
import com.readup.server.book.infrastructure.client.bookinfo.nationallibraryofkorea.dto.BookDetailResponse;

import lombok.Builder;

@Builder
public record BookInfoVO(
	String bookTitle,
	String publisher,
	String author,
	String isbn,
	String titleUrl,
	String summary,
	String chapter
) {
	public static BookInfoVO from(BookDetailResponse bookDetailResponse) {
		return BookInfoVO.builder()
			.bookTitle(bookDetailResponse.title())
			.publisher(bookDetailResponse.publisher())
			.author(bookDetailResponse.author())
			.isbn(bookDetailResponse.eaIsbn())
			.titleUrl(bookDetailResponse.titleUrl())
			.chapter(bookDetailResponse.bookTbCnt())
			.build();
	}

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
