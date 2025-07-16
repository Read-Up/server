package com.readup.server.quiz.infrastructure.persistence.sort;

import static com.readup.server.common.exception.ErrorCode.*;
import static com.readup.server.quiz.domain.model.QQuizSet.*;

import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.readup.server.common.exception.DomainException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuizSetSortType implements SortType {

	LIKE_AVERAGE("likeAverage", quizSet.likeAverage),
	CORRECT_ANSWER_AVERAGE("correctAnswerAverage", quizSet.correctAnswerAverage),
	CREATED_AT("createdAt", quizSet.createdAt);

	private final String fieldName;
	private final ComparableExpressionBase<?> expression;

	public static ComparableExpressionBase<?> getExpressionByFieldName(String fieldName) {
		return SortType.getExpressionByFieldName(QuizSetSortType.class, fieldName,
			new DomainException(BAD_QUIZ_SET_SORT));
	}
}