package com.readup.server.book.infrastructure.client.bookinfo.nationallibraryofkorea;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.readup.server.book.infrastructure.client.bookinfo.BookInfoClientFacade;
import com.readup.server.book.infrastructure.client.bookinfo.nationallibraryofkorea.dto.BookDetail;
import com.readup.server.book.infrastructure.client.bookinfo.nationallibraryofkorea.dto.GetBookNationalLibraryOfKoreaResponse;
import com.readup.server.book.infrastructure.client.bookinfo.nationallibraryofkorea.feign.BookInfoNationalLibraryOfKoreaFeignClient;
import com.readup.server.book.infrastructure.client.bookinfo.vo.BookInfoVO;
import com.readup.server.common.exception.ErrorCode;
import com.readup.server.common.exception.FeignException;

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

		if (Objects.equals(getBookNationalLibraryOfKoreaResponse.totalCount(), "0")) {
			throw new FeignException(ErrorCode.EXTERNAL_BOOK_INFO_NOT_FOUND);
		}

		BookDetail bookDetail = getBookNationalLibraryOfKoreaResponse.getBookDetail();

		return BookInfoVO.from(bookDetail);
	}

	@Override
	public String getServiceName() {
		return nationalLibraryOfKoreaProperties.getServiceName();
	}
}
