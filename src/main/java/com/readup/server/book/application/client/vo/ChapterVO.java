package com.readup.server.book.application.client.vo;

import com.readup.server.book.domain.Chapter;

import lombok.Builder;

@Builder
public record ChapterVO(
	String name,
	Integer chapterNumber
) {
	public Chapter toEntity() {
		return Chapter.builder()
			.name(name)
			.chapterNumber(chapterNumber)
			.build();
	}
}
