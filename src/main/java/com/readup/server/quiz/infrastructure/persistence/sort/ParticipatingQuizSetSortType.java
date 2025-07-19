package com.readup.server.quiz.infrastructure.persistence.sort;

import static com.readup.server.common.exception.ErrorCode.*;
import static com.readup.server.quiz.domain.model.QQuizSet.*;
import static com.readup.server.user_quiz.domain.model.QUserQuizSet.*;

import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.readup.server.common.exception.DomainException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ParticipatingQuizSetSortType implements SortType {

	SOLVED_AT("solvedAt", userQuizSet.updatedAt),
	CREATED_AT("createdAt", quizSet.createdAt),
	LIKE_AVERAGE("likeAverage", quizSet.likeAverage),
	CORRECT_ANSWER_AVERAGE("correctAnswerAverage", quizSet.correctAnswerAverage);

	private final String fieldName;
	private final ComparableExpressionBase<?> expression;

	public static ComparableExpressionBase<?> getExpressionByFieldName(String fieldName) {
		return SortType.getExpressionByFieldName(ParticipatingQuizSetSortType.class, fieldName,
			new DomainException(BAD_PARTICIPATING_QUIZ_SET_SORT));
	}
}