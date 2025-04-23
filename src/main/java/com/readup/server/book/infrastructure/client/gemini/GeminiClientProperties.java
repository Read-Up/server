package com.readup.server.book.infrastructure.client.gemini;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import lombok.Getter;

@Getter
@ConfigurationProperties(prefix = "gemini-client")
public class GeminiClientProperties {
	private final String apiKey;
	private final String parseChapterPromptTemplate;

	@ConstructorBinding
	public GeminiClientProperties(String apiKey, String parseChapterPromptTemplate) {
		this.apiKey = apiKey;
		this.parseChapterPromptTemplate = parseChapterPromptTemplate;
	}
}
