package com.readup.server.book.infrastructure.client.bookinfo.nationallibraryofkorea.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetBookNationalLibraryOfKoreaResponse {
	@JsonProperty("TOTAL_COUNT")
	private String totalCount;

	@JsonProperty("PAGE_NO")
	private String pageNo;

	@JsonProperty("docs")
	private List<BookDetail> docs;
}

