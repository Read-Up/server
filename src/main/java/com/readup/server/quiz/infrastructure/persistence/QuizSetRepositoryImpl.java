package com.readup.server.quiz.infrastructure.persistence;

import static com.readup.server.common.exception.ErrorCode.*;

import org.springframework.stereotype.Repository;

import com.readup.server.common.exception.RepositoryException;
import com.readup.server.quiz.domain.model.QuizSet;
import com.readup.server.quiz.domain.repository.QuizSetRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QuizSetRepositoryImpl implements QuizSetRepository {

	private final QuizSetJpaRepository quizSetJpaRepository;

	@Override
	public QuizSet save(QuizSet quizSet) {
		return quizSetJpaRepository.save(quizSet);
	}

	@Override
	public QuizSet getQuizSetById(Long quizSetId) {
		return quizSetJpaRepository.findById(quizSetId)
			.orElseThrow(() -> new RepositoryException(NOT_FOUND_QUIZ_SET));
	}
}
