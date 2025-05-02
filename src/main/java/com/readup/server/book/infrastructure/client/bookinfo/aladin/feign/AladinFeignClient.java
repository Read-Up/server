package com.readup.server.book.infrastructure.client.bookinfo.aladin.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
	name = "aladinClient",
	url = "${book-info-client.aladin.url}"
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
