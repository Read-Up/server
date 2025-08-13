package com.readup.server.user_quiz.application.dto;

public record SubmitUserQuizResponse(Boolean isCorrect, String explanation) {

	public static SubmitUserQuizResponse of(Boolean isCorrect, String explanation) {
		return new SubmitUserQuizResponse(isCorrect, explanation);
	}
}
