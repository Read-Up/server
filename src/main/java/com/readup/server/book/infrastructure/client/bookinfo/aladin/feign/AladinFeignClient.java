package com.readup.server.book.infrastructure.client.bookinfo.aladin.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
	name = "aladin",
	url = "http://www.aladin.co.kr/ttb/api"
)
public interface AladinFeignClient {

	@GetMapping("/ItemLookUp.aspx")
	String getBookInfoByIsbn(
		@RequestParam("TTBKey") String ttbKey,
		@RequestParam("ItemId") String isbn,
		@RequestParam("ItemIdType") String itemIdType,
		@RequestParam("Output") String output
	);
}
