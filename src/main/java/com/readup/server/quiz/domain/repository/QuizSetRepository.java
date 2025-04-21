package com.readup.server.quiz.domain.repository;

import com.readup.server.quiz.domain.model.QuizSet;

public interface QuizSetRepository {
	QuizSet save(QuizSet quizSet);
}