package com.readup.server.book.infrastructure.client.bookinfo.nationallibraryofkorea.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GetBookNationalLibraryOfKoreaResponse(
	@JsonProperty("TOTAL_COUNT")
	String totalCount,

	@JsonProperty("PAGE_NO")
	String pageNo,

	@JsonProperty("docs")
	List<BookDetail> docs
) {
	public BookDetail getBookDetail() {
		return docs.getFirst();
	}
}
