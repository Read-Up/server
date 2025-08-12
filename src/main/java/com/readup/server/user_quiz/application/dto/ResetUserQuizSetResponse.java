package com.readup.server.user_quiz.application.dto;

public record ResetUserQuizSetResponse(Long userQuizSetId, Long quizSetId) {

	public static ResetUserQuizSetResponse of(Long userQuizSetId, Long quizSetId) {
		return new ResetUserQuizSetResponse(userQuizSetId, quizSetId);
	}
}
