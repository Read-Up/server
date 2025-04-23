package com.readup.server.book.infrastructure.client.gemini.dto;

import java.util.List;

public record GeminiResponse(
	List<Candidate> candidates,
	String role,
	String finishReason,
	int index
) {

	public String getText() {
		return candidates.stream()
			.map(Candidate::content)
			.flatMap(content -> content.parts().stream())
			.map(Part::text)
			.reduce("", String::concat);
	}

	public record Candidate(
		Content content,
		String finishReason
	) {
	}

	public record Content(
		List<Part> parts
	) {
	}

	public record Part(
		String text
	) {
	}
}
