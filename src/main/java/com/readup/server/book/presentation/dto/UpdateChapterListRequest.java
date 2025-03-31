package com.readup.server.book.presentation.dto;

import java.util.List;

import com.readup.server.book.domain.Chapter;

import jakarta.validation.constraints.NotNull;

public record UpdateChapterListRequest(
	@NotNull Long bookId,
	@NotNull List<UpdateChapterRequest> chapters
) {

	public List<Chapter> toChapterList() {
		return chapters.stream()
			.map(UpdateChapterRequest::toEntity)
			.toList();
	}
}
