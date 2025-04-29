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
	List<String> chapterList
) {
	public static RetrieveBookResponse from(Book book) {
		return RetrieveBookResponse.builder()
			.bookId(book.getId())
			.bookTitle(book.getTitle())
			.publisher(book.getPublisher())
			.author(book.getAuthor())
			.isbn(book.getIsbn())
			.chapterList(book.getChapterList().stream().map(Chapter::getName).toList())
			.build();
	}

	@Builder
	public record ChapterResponse(
		Long chapterId,
		int chapterOrder,
		String chapterName
	) {
		public static ChapterResponse from(Chapter chapter) {
			return ChapterResponse.builder()
				.chapterId(chapter.getId())
				.chapterOrder(chapter.getChapterOrder())
				.chapterName(chapter.getName())
				.build();
		}
	}
}
