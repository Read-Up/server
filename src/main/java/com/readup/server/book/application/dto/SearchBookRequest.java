package com.readup.server.book.application.dto;

public record SearchBookRequest(
	String title,
	String isbn
) {
}
