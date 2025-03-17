package com.readup.server.book.application;

import org.springframework.stereotype.Service;

import com.readup.server.book.infrastructure.client.bookinfo.BookInfoClientFactory;
import com.readup.server.book.infrastructure.client.bookinfo.vo.BookInfoVO;
import com.readup.server.book.presentation.dto.GetExternalBookResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookInfoService {
	private static final String SERVICE_NAME = "NationalLibraryOfKorea";
	private final BookInfoClientFactory bookInfoClientFactory;

	public GetExternalBookResponse getBookInfo(String isbn) {
		BookInfoVO bookInfoVO = bookInfoClientFactory.getBookInfoClient(SERVICE_NAME).getBookInfo(isbn);
	}
}
