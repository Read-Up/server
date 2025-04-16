package com.readup.server.book.presentation.dto;

public record SearchBookRequest(
	String title,
	String isbn
) {
}
