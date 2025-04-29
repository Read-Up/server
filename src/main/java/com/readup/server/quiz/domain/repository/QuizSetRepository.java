package com.readup.server.quiz.domain.repository;

import java.util.Optional;

import com.readup.server.quiz.domain.model.QuizSet;

public interface QuizSetRepository {
	QuizSet save(QuizSet quizSet);

	Optional<QuizSet> findById(Long quizSetId);
}