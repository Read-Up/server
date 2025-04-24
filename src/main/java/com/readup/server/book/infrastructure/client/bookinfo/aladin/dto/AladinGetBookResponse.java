package com.readup.server.book.infrastructure.client.bookinfo.aladin.dto;

import java.util.List;
import java.util.Optional;

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
		if (item == null || item.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(item.getFirst());
	}
}
