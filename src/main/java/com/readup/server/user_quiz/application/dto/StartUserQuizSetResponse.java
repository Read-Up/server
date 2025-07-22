package com.readup.server.user_quiz.application.dto;

import java.time.LocalDateTime;

public record StartUserQuizSetResponse(Long userQuizSetId, Long quizSetId, LocalDateTime startedAt) {

	public static StartUserQuizSetResponse of(Long userQuizSetId, Long quizSetId, LocalDateTime startedAt) {
		return new StartUserQuizSetResponse(userQuizSetId, quizSetId, startedAt);
	}
}
