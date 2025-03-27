package com.readup.server.book.presentation.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record UpdateChapterListRequest(
	@NotNull Long bookId,
	@NotNull List<UpdateChapterRequest> chapters
) {
}
