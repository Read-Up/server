package com.readup.server.user_quiz.application.dto;

import com.readup.server.user_quiz.domain.model.UserQuizSet;

public record GetUserQuizSetResponse(Long userQuizSetId, int quizSequence, Boolean isEvaluated) {

	public static GetUserQuizSetResponse from(UserQuizSet userQuizSet) {
		return new GetUserQuizSetResponse(userQuizSet.getId(), userQuizSet.getQuizSequence(), userQuizSet.getIsEvaluated());
	}
}
