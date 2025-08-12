package com.readup.server.user_quiz.application.dto;

import com.readup.server.user_quiz.domain.model.UserQuizSet;
import com.readup.server.user_quiz.domain.model.UserQuizSetStatus;

public record EvaluateQuizSetResponse(Long userQuizSetId, Long quizSetId, Boolean isEvaluated,
									  UserQuizSetStatus status, Double correctAnswerAverage, Integer likeScore) {

	public static EvaluateQuizSetResponse from(UserQuizSet userQuizSet) {
		return new EvaluateQuizSetResponse(userQuizSet.getId(), userQuizSet.getQuizSetId(),
			userQuizSet.getIsEvaluated(), userQuizSet.getStatus(), userQuizSet.getCorrectAnswerAverage(),
			userQuizSet.getLikeScore());
	}
}
