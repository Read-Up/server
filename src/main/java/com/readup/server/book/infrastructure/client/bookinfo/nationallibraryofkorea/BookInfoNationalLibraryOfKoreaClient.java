package com.readup.server.book.infrastructure.client.bookinfo.nationallibraryofkorea;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.readup.server.book.infrastructure.client.bookinfo.BookInfoClient;
import com.readup.server.book.infrastructure.client.bookinfo.nationallibraryofkorea.dto.BookDetail;
import com.readup.server.book.infrastructure.client.bookinfo.nationallibraryofkorea.dto.GetBookNationalLibraryOfKoreaResponse;
import com.readup.server.book.infrastructure.client.bookinfo.vo.BookInfoVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookInfoNationalLibraryOfKoreaClient implements BookInfoClient {

	private static final String SERVICE_NAME = "NationalLibraryOfKorea";
	private static final String CERT_KEY = "8232ebc025bba7cf95003b64a6aa560fb0a4195f5f04414055848499e05c8534";
	private final BookInfoNationalLibraryOfKoreaFeignClient bookInfoNationalLibraryOfKoreaFeignClient;

	@Override
	public BookInfoVO getBookInfo(String isbn) {
		
		ResponseEntity<GetBookNationalLibraryOfKoreaResponse> getBookNationalLibraryOfKoreaResponse
			= bookInfoNationalLibraryOfKoreaFeignClient.getBookInfoByIsbn(CERT_KEY, isbn, "json", 1, 10);

		BookDetail bookDetail = getBookNationalLibraryOfKoreaResponse.getBody().getDocs().getFirst();

		if (Objects.isNull(bookDetail)) {
			return null;
		}

		return BookInfoVO.builder()
			.bookTitle(bookDetail.getTitle())
			.publisher(bookDetail.getPublisher())
			.author(bookDetail.getAuthor())
			.isbn(bookDetail.getEaIsbn())
			.tableOfContentsUrl(bookDetail.getBookTbCntUrl())
			.build();
	}

	@Override
	public String getServiceName() {
		return SERVICE_NAME;
	}
}
