package com.readup.server.book.infrastructure.client.bookinfo.aladin;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import lombok.Getter;

@Getter
@ConfigurationProperties(prefix = "book-info-client.aladin")
public class AladinProperties {

	private final String serviceName;
	private final String ttbKey;

	@ConstructorBinding
	public AladinProperties(String serviceName, String ttbKey) {
		this.serviceName = serviceName;
		this.ttbKey = ttbKey;
	}
}
