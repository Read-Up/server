package com.readup.server.user_quiz.application.dto;

import java.time.LocalDateTime;

public record CompleteUserQuizSetResponse(Long userQuizSetId, Long quizSetId, LocalDateTime completedAt) {

	public static CompleteUserQuizSetResponse of(Long userQuizSetId, Long quizSetId, LocalDateTime completedAt) {
		return new CompleteUserQuizSetResponse(userQuizSetId, quizSetId, completedAt);
	}
}
