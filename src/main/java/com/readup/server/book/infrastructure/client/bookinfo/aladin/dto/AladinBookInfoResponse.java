package com.readup.server.book.infrastructure.client.bookinfo.aladin.dto;

import java.util.List;

public record AladinBookInfoResponse(
	String subTitle,
	String originalTitle,
	int itemPage,
	String toc,
	List<String> letslookimg,
	List<AladinAuthorResponse> authors,
	List<AladinEBookResponse> ebookList
) {
}
