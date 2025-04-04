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

	private static final String RESULT_STYLE = "json";
	private static final String INVALID_TOTAL_COUNT = "0";
	private static final int PAGE_NO = 1;
	private static final int PAGE_SIZE = 10;
	private final NationalLibraryOfKoreaProperties nationalLibraryOfKoreaProperties;
	private final BookInfoNationalLibraryOfKoreaFeignClient bookInfoNationalLibraryOfKoreaFeignClient;

	@Override
	public BookInfoVO getBookInfo(String isbn) {

		GetBookNationalLibraryOfKoreaResponse getBookNationalLibraryOfKoreaResponse
			= bookInfoNationalLibraryOfKoreaFeignClient.getBookInfoByIsbn(
			nationalLibraryOfKoreaProperties.getCertKey(), isbn, RESULT_STYLE, PAGE_NO, PAGE_SIZE);

		if (Objects.equals(getBookNationalLibraryOfKoreaResponse.totalCount(), INVALID_TOTAL_COUNT)) {
			throw new FeignException(ErrorCode.EXTERNAL_BOOK_INFO_NOT_FOUND);
		}

		BookDetail bookDetail = getBookNationalLibraryOfKoreaResponse.getBookDetail()
			.orElseThrow(() -> new FeignException(ErrorCode.EXTERNAL_BOOK_INFO_NOT_FOUND));

		return BookInfoVO.from(bookDetail);
	}

	@Override
	public String getServiceName() {
		return nationalLibraryOfKoreaProperties.getServiceName();
	}
}
