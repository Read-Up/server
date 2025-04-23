package com.readup.server.book.infrastructure.client.gemini.dto;

import java.util.List;

import lombok.Builder;

public record GeminiRequest(
	SystemInstruction systemInstruction,
	List<Content> contents
) {

	@Builder
	public GeminiRequest(String systemInstruction, String contents) {
		this(new SystemInstruction(systemInstruction), List.of(new Content(List.of(new Part(contents)))));
	}

	private record SystemInstruction(
		List<Part> parts
	) {
		public SystemInstruction(String systemInstruction) {
			this(List.of(new Part(systemInstruction)));
		}
	}

	private record Content(
		List<Part> parts
	) {
	}

	private record Part(
		String text
	) {
	}

}
