package com.readup.server.book.infrastructure.client.bookinfo.nationallibraryofkorea;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.readup.server.book.infrastructure.client.bookinfo.nationallibraryofkorea.dto.GetBookNationalLibraryOfKoreaResponse;

@FeignClient(name = "nationallibraryofkorea", url = "https://www.nl.go.kr/seoji")
public interface BookInfoNationalLibraryOfKoreaFeignClient {

	@GetMapping("/SearchApi.do")
	GetBookNationalLibraryOfKoreaResponse getBookInfoByIsbn(
		@RequestParam("cert_key") String certKey,
		@RequestParam("isbn") String isbn,
		@RequestParam("result_style") String resultStyle,
		@RequestParam("page_no") int pageNo,
		@RequestParam("page_size") int pageSize
	);

}
