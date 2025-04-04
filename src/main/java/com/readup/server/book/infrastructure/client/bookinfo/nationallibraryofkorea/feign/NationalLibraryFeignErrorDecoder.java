package com.readup.server.book.infrastructure.client.bookinfo.nationallibraryofkorea.feign;

import com.readup.server.common.exception.ErrorCode;
import com.readup.server.common.exception.FeignException;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NationalLibraryFeignErrorDecoder implements ErrorDecoder {

	@Override
	public Exception decode(String methodKey, Response response) {

		return switch (response.status()) {
			case 404 -> new FeignException(ErrorCode.EXTERNAL_BOOK_INFO_NOT_FOUND);
			case 500 -> {
				log.warn("Feign error from National Library API: status={}, methodKey={}", response.status(),
					methodKey);
				yield new FeignException(ErrorCode.API_SERVER_ERROR);
			}
			default -> null;
		};
	}
}