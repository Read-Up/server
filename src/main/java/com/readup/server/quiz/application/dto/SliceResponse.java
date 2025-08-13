package com.readup.server.quiz.application.dto;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

public record SliceResponse<T>(List<T> content, int requestContentSize, int responseContentSize, int currentPageNumber,
							   boolean isFirst, boolean isLast, List<SortResponse> sortList) {

	public static <T> SliceResponse<T> from(Slice<T> response) {
		return new SliceResponse<>(
			response.getContent(),
			response.getSize(),
			response.getNumberOfElements(),
			response.getNumber(),
			response.isFirst(),
			response.isLast(),
			SortResponse.from(response.getPageable().getSort()));
	}

	public record SortResponse(String field, Sort.Direction direction) {

		public static List<SortResponse> from(Sort sort) {
			return sort.stream()
				.map(s -> new SortResponse(s.getProperty(), s.getDirection()))
				.toList();
		}
	}
}
