package com.readup.server.book.infrastructure.client.bookinfo.vo;

import com.readup.server.book.domain.Chapter;

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
