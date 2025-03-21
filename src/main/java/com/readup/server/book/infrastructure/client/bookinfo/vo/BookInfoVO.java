package com.readup.server.book.infrastructure.client.bookinfo.vo;

import java.util.List;

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
	List<ChapterVO> chapterList
) {
	public Book toEntity() {
		return Book.builder()
			.title(bookTitle)
			.author(author)
			.publisher(publisher)
			.isbn(isbn)
			.titleUrl(titleUrl)
			.chapterList(chapterList.stream().map(ChapterVO::toEntity).toList())
			.build();
	}
}
