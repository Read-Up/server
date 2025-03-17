package com.readup.server.book.infrastructure.client.bookinfo;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookInfoClientFactory {
	private final List<BookInfoClient> bookInfoClientList;

	public BookInfoClient getBookInfoClient(String serviceName) {
		return bookInfoClientList.stream()
			.filter(client -> client.getServiceName().equals(serviceName))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Not supported service name: " + serviceName));
	}
}
