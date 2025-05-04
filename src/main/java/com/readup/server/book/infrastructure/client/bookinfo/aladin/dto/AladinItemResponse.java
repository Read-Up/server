package com.readup.server.book.infrastructure.client.bookinfo.aladin.dto;

import com.readup.server.book.application.client.vo.BookInfoVO;

public record AladinItemResponse(
	String title,
	String link,
	String author,
	String pubDate,
	String description,
	String creator,
	String isbn,
	String isbn13,
	int itemId,
	int priceSales,
	int priceStandard,
	String stockStatus,
	int mileage,
	String cover,
	int categoryId,
	String categoryName,
	String publisher,
	int customerReviewRank,
	AladinBookInfoResponse bookinfo
) {
	public BookInfoVO toBookInfoVO() {
		return BookInfoVO.builder()
			.bookTitle(title)
			.publisher(publisher)
			.author(author)
			.isbn(isbn13)
			.titleUrl(cover)
			.summary(description)
			.rawChapter(bookinfo.toc())
			.build();
	}
}
