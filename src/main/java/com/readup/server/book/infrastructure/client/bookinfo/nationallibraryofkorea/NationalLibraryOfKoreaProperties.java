package com.readup.server.book.infrastructure.client.bookinfo.nationallibraryofkorea;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import lombok.Getter;

@Getter
@ConfigurationProperties(prefix = "book-info-client.national-library")
public class NationalLibraryOfKoreaProperties {
	private final String serviceName;
	private final String certKey;

	@ConstructorBinding
	public NationalLibraryOfKoreaProperties(String serviceName, String certKey) {
		this.serviceName = serviceName;
		this.certKey = certKey;
	}
}
