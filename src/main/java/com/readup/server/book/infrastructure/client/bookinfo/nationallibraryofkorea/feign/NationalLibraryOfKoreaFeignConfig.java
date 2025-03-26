package com.readup.server.book.infrastructure.client.bookinfo.nationallibraryofkorea.feign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NationalLibraryOfKoreaFeignConfig {

	@Bean
	public NationalLibraryFeignErrorDecoder nationalLibraryFeignErrorDecoder() {
		return new NationalLibraryFeignErrorDecoder();
	}
}
