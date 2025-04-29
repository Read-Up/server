package com.readup.server.book.application.dto;

import java.util.List;

import com.readup.server.book.domain.Book;
import com.readup.server.book.domain.Chapter;

import lombok.Builder;

@Builder
public record GetBookResponse(
	Long bookId,
	String title,
	String author,
	String publisher,
	String isbn,
	String titleUrl,
	List<ChapterResponse> chapterList
) {
	public static GetBookResponse from(
		Book book
	) {
		return GetBookResponse.builder()
			.bookId(book.getId())
			.title(book.getTitle())
			.author(book.getAuthor())
			.publisher(book.getPublisher())
			.isbn(book.getIsbn())
			.titleUrl(book.getTitleUrl())
			.chapterList(book.getChapterList().stream()
				.map(ChapterResponse::from)
				.toList())
			.build();
	}

	@Builder
	public record ChapterResponse(
		Long chapterId,
		int chapterOrder,
		String chapterName
	) {
		public static ChapterResponse from(
			Chapter chapter
		) {
			return ChapterResponse.builder()
				.chapterId(chapter.getId())
				.chapterOrder(chapter.getChapterOrder())
				.chapterName(chapter.getName())
				.build();
		}
	}
}
