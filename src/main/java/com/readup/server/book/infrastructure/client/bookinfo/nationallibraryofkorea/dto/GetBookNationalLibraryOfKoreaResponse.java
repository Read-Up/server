package com.readup.server.book.infrastructure.client.bookinfo.nationallibraryofkorea.dto;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GetBookNationalLibraryOfKoreaResponse(
	@JsonProperty("TOTAL_COUNT")
	String totalCount,

	@JsonProperty("PAGE_NO")
	String pageNo,

	@JsonProperty("docs")
	List<BookDetailResponse> docs
) {
	public Optional<BookDetailResponse> getBookDetail() {
		if (docs == null || docs.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(docs.getFirst());
	}
}
