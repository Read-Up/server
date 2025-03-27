package com.readup.server.book.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateChapterRequest(
	@NotNull Integer chapterNumber,
	@NotBlank String chapterName
) {
}
