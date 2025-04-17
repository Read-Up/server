package com.readup.server.book.application.client.vo;

import java.util.List;

import com.readup.server.book.domain.Book;
import com.readup.server.book.infrastructure.client.bookinfo.nationallibraryofkorea.dto.BookDetail;

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
	public static BookInfoVO from(BookDetail bookDetail) {
		return BookInfoVO.builder()
			.bookTitle(bookDetail.title())
			.publisher(bookDetail.publisher())
			.author(bookDetail.author())
			.isbn(bookDetail.eaIsbn())
			.titleUrl(bookDetail.titleUrl())
			.chapterList(List.of())
			.build();
	}

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
