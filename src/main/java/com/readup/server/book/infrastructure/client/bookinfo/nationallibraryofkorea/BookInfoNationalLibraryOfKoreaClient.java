package com.readup.server.book.infrastructure.client.bookinfo.nationallibraryofkorea;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.readup.server.book.infrastructure.client.bookinfo.BookInfoClientFacade;
import com.readup.server.book.infrastructure.client.bookinfo.nationallibraryofkorea.dto.BookDetail;
import com.readup.server.book.infrastructure.client.bookinfo.nationallibraryofkorea.dto.GetBookNationalLibraryOfKoreaResponse;
import com.readup.server.book.infrastructure.client.bookinfo.vo.BookInfoVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookInfoNationalLibraryOfKoreaClient implements BookInfoClientFacade {

	private final NationalLibraryOfKoreaProperties nationalLibraryOfKoreaProperties;
	private final BookInfoNationalLibraryOfKoreaFeignClient bookInfoNationalLibraryOfKoreaFeignClient;

	@Override
	public BookInfoVO getBookInfo(String isbn) {

		GetBookNationalLibraryOfKoreaResponse getBookNationalLibraryOfKoreaResponse
			= bookInfoNationalLibraryOfKoreaFeignClient.getBookInfoByIsbn(
			nationalLibraryOfKoreaProperties.getCertKey(), isbn, "json", 1, 10);

		BookDetail bookDetail = getBookNationalLibraryOfKoreaResponse.getBookDetail();

		if (Objects.isNull(bookDetail)) {
			return null;
		}

		return BookInfoVO.builder()
			.bookTitle(bookDetail.getTitle())
			.publisher(bookDetail.getPublisher())
			.author(bookDetail.getAuthor())
			.isbn(bookDetail.getEaIsbn())
			.titleUrl(bookDetail.getTitleUrl())
			.chapterList(List.of())
			.build();
	}

	@Override
	public String getServiceName() {
		return nationalLibraryOfKoreaProperties.getServiceName();
	}
}
