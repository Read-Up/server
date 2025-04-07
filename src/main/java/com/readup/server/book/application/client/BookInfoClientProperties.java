package com.readup.server.book.application.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import lombok.Getter;

@Getter
@ConfigurationProperties(prefix = "book-info-client")
public class BookInfoClientProperties {
	private final String defaultServiceName;

	@ConstructorBinding
	public BookInfoClientProperties(String defaultServiceName) {
		this.defaultServiceName = defaultServiceName;
	}
}
