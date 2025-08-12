package com.readup.server.user_quiz.application.dto;

public record CompleteUserQuizSetResponse(Long userQuizSetId, Long quizSetId) {

	public static CompleteUserQuizSetResponse of(Long userQuizSetId, Long quizSetId) {
		return new CompleteUserQuizSetResponse(userQuizSetId, quizSetId);
	}
}
