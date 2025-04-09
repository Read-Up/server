package com.readup.server.book.presentation.dto;

import java.util.List;

import com.readup.server.book.domain.Chapter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateChapterListRequest(
	@NotNull List<UpdateChapterRequest> chapters
) {

	public List<Chapter> toChapterList() {
		return chapters.stream()
			.map(UpdateChapterRequest::toEntity)
			.toList();
	}

	public record UpdateChapterRequest(
		@NotNull Integer chapterNumber,
		@NotBlank String chapterName
	) {
		public Chapter toEntity() {
			return Chapter.builder()
				.chapterNumber(chapterNumber)
				.name(chapterName)
				.build();
		}
	}
}
