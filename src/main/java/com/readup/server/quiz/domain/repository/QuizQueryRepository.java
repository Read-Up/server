package com.readup.server.quiz.domain.repository;

import com.readup.server.quiz.domain.model.Quiz;

public interface QuizQueryRepository {
	Quiz getQuizWithQuizOptionById(Long quizSetId, Long quizId);
}