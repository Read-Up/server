package com.readup.server.book.infrastructure.client.bookinfo.vo;

import lombok.Builder;

@Builder
public record BookInfoVO(
	String bookTitle,
	String publisher,
	String author,
	String isbn,
	String tableOfContentsUrl
) {
}
