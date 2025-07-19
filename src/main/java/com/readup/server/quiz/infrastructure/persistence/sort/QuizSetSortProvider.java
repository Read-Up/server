package com.readup.server.quiz.infrastructure.persistence.sort;

import java.util.function.Function;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;

public final class QuizSetSortProvider {

	private QuizSetSortProvider() {
	}

	public static OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {
		return getOrderSpecifiers(pageable, QuizSetSortType::getExpressionByFieldName);
	}

	public static OrderSpecifier<?>[] getParticipatingOrderSpecifiers(Pageable pageable) {
		return getOrderSpecifiers(pageable, ParticipatingQuizSetSortType::getExpressionByFieldName);
	}

	private static OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable,
		Function<String, ComparableExpressionBase<?>> expressionProvider) {
		return pageable.getSort()
			.stream()
			.map(order -> createOrderSpecifier(order, expressionProvider))
			.toArray(OrderSpecifier[]::new);
	}

	private static OrderSpecifier<?> createOrderSpecifier(Sort.Order order,
		Function<String, ComparableExpressionBase<?>> expressionProvider) {
		return new OrderSpecifier<>(getDirection(order),
			expressionProvider.apply(order.getProperty()));
	}

	private static Order getDirection(Sort.Order order) {
		return order.isAscending() ? Order.ASC : Order.DESC;
	}
}