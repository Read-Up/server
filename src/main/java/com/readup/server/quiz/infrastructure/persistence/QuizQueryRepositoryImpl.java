package com.readup.server.quiz.infrastructure.persistence;

import static com.readup.server.common.exception.ErrorCode.*;
import static com.readup.server.quiz.domain.model.QQuiz.*;
import static com.readup.server.quiz.domain.model.QQuizOption.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.readup.server.common.exception.RepositoryException;
import com.readup.server.quiz.domain.model.Quiz;
import com.readup.server.quiz.domain.repository.QuizQueryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QuizQueryRepositoryImpl implements QuizQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Quiz getQuizWithQuizOptionById(Long quizSetId, Long quizId) {
		return findQuizWithQuizOptionById(quizSetId, quizId)
			.orElseThrow(() -> new RepositoryException(NOT_FOUND_QUIZ));
	}

	private Optional<Quiz> findQuizWithQuizOptionById(Long quizSetId, Long quizId) {
		return Optional.ofNullable(queryFactory.selectFrom(quiz)
			.innerJoin(quiz.quizOptionList, quizOption).fetchJoin()
			.where(quiz.quizSet.id.eq(quizSetId).and(quiz.id.eq(quizId)))
			.fetchOne());
	}
}
