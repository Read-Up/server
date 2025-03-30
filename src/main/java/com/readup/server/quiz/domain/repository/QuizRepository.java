package com.readup.server.quiz.domain.repository;

import com.readup.server.quiz.domain.model.Quiz;

public interface QuizRepository {
	Quiz save(Quiz quiz);
}
