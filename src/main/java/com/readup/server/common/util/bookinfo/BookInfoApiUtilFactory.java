package com.readup.server.common.util.bookinfo;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookInfoApiUtilFactory {
	private List<BookInfoApiUtil> bookInfoApiUtils;

	public BookInfoApiUtil getBookInfoApiUtil(String serviceName) {
		for (BookInfoApiUtil bookInfoApiUtil : bookInfoApiUtils) {
			if (bookInfoApiUtil.getServiceName().equals(serviceName)) {
				return bookInfoApiUtil;
			}
		}
		return null;
	}
}
