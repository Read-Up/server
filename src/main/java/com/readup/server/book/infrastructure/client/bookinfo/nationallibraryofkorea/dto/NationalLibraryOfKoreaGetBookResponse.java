package com.readup.server.book.infrastructure.client.bookinfo.nationallibraryofkorea.dto;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.jsonwebtoken.lang.Objects;

public record NationalLibraryOfKoreaGetBookResponse(
	@JsonProperty("TOTAL_COUNT")
	String totalCount,

	@JsonProperty("PAGE_NO")
	String pageNo,

	@JsonProperty("docs")
	List<NationalLibraryOfKoreaBookDetailResponse> docs
) {
	public Optional<NationalLibraryOfKoreaBookDetailResponse> getBookDetail() {
		if (Objects.isEmpty(docs)) {
			return Optional.empty();
		}
		return Optional.of(docs.getFirst());
	}
}
