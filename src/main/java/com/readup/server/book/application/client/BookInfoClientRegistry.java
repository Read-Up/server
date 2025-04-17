package com.readup.server.book.application.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookInfoClientRegistry {
	private final List<BookInfoClientFacade> bookInfoClientFacadeList;
	private final Map<String, BookInfoClientFacade> clientMap = new HashMap<>();
	private final BookInfoClientProperties bookInfoClientProperties;

	@PostConstruct
	public void init() {
		bookInfoClientFacadeList.forEach(client -> clientMap.put(client.getServiceName(), client));
	}

	public BookInfoClientFacade getBookInfoClient(String serviceName) {
		BookInfoClientFacade client = clientMap.get(serviceName);
		if (client == null) {
			throw new IllegalArgumentException("Not supported service name: " + serviceName);
		}
		return client;
	}

	public BookInfoClientFacade getDefaultBookInfoClient() {
		return getBookInfoClient(bookInfoClientProperties.getDefaultServiceName());
	}
}
