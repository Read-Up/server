package com.readup.server.book.infrastructure.client.bookinfo.nationallibraryofkorea.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.readup.server.book.infrastructure.client.bookinfo.nationallibraryofkorea.dto.NationalLibraryOfKoreaGetBookResponse;

@FeignClient(name = "nationalLibraryOfKoreaClient", url = "${book-info-client.national-library.url}", configuration = NationalLibraryFeignErrorDecoder.class)
public interface BookInfoNationalLibraryOfKoreaFeignClient {

	@GetMapping("/SearchApi.do")
	NationalLibraryOfKoreaGetBookResponse getBookInfoByIsbn(
		@RequestParam("cert_key") String certKey,
		@RequestParam("isbn") String isbn,
		@RequestParam("result_style") String resultStyle,
		@RequestParam("page_no") int pageNo,
		@RequestParam("page_size") int pageSize
	);

}
