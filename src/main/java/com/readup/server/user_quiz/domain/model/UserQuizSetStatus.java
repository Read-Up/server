package com.readup.server.user_quiz.domain.model;

import static com.readup.server.common.exception.ErrorCode.*;

import com.readup.server.common.exception.DomainException;

public enum UserQuizSetStatus {
	IN_PROGRESS,
	COMPLETED;

	public static UserQuizSetStatus fromString(String value) {
		if ("ALL".equals(value)) {
			return null;
		}

		for (UserQuizSetStatus userQuizSetStatus : UserQuizSetStatus.values()) {
			if (userQuizSetStatus.name().equals(value)) {
				return userQuizSetStatus;
			}
		}

		throw new DomainException(INVALID_PARAMETER);
	}
}
