package com.readup.server.book.infrastructure.client.bookinfo.aladin.dto;

public record AladinEBookResponse(
	int itemId,
	String isbn,
	int priceSales,
	String link
) {
}
