package com.readup.server.book.infrastructure.client.bookinfo.aladin.dto;

import java.util.List;
import java.util.Optional;

import io.jsonwebtoken.lang.Objects;

public record AladinGetBookResponse(
	String version,
	String title,
	String link,
	String pubDate,
	String imageUrl,
	int totalResults,
	int startIndex,
	int itemsPerPage,
	String query,
	int searchCategoryId,
	String searchCategoryName,
	List<AladinItemResponse> item
) {
	public Optional<AladinItemResponse> getAladinItemResponse() {
		if (Objects.isEmpty(item)) {
			return Optional.empty();
		}
		return Optional.of(item.getFirst());
	}
}
