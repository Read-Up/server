package com.readup.server.book.infrastructure.client.bookinfo.aladin;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.readup.server.book.application.client.BookInfoClientFacade;
import com.readup.server.book.application.client.vo.BookInfoVO;
import com.readup.server.book.infrastructure.client.bookinfo.aladin.dto.AladinGetBookResponse;
import com.readup.server.book.infrastructure.client.bookinfo.aladin.dto.AladinItemResponse;
import com.readup.server.book.infrastructure.client.bookinfo.aladin.feign.AladinFeignClient;
import com.readup.server.common.exception.ErrorCode;
import com.readup.server.common.exception.FeignException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AladinClient implements BookInfoClientFacade {

	private static final String RESULT_STYLE = "JS";
	private static final String ITEM_ID_TYPE = "ISBN13";
	private static final int INVALID_TOTAL_COUNT = 1;
	private final AladinFeignClient aladinFeignClient;
	private final AladinProperties aladinProperties;

	@Override
	public BookInfoVO getBookInfo(String isbn) {
		AladinGetBookResponse aladinGetBookResponse = aladinFeignClient.getBookInfoByIsbn(
			aladinProperties.getTtbKey(),
			isbn,
			ITEM_ID_TYPE,
			RESULT_STYLE
		);

		if (Objects.equals(aladinGetBookResponse.totalResults(), INVALID_TOTAL_COUNT)) {
			throw new FeignException(ErrorCode.EXTERNAL_BOOK_INFO_NOT_FOUND);
		}

		AladinItemResponse aladinItemResponse =
			aladinGetBookResponse.getAladinItemResponse()
				.orElseThrow(() -> new FeignException(ErrorCode.EXTERNAL_BOOK_INFO_NOT_FOUND));

		return aladinItemResponse.toBookInfoVO();
	}

	@Override
	public String getServiceName() {
		return aladinProperties.getServiceName();
	}
}
