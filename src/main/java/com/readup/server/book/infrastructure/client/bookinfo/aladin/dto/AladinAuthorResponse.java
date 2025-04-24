package com.readup.server.book.infrastructure.client.bookinfo.aladin.dto;

public record AladinAuthorResponse(
	String authorType,
	int authorid,
	String desc,
	String name
) {
}
