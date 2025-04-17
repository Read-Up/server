package com.readup.server.quiz.domain.repository;

import java.util.List;

import com.readup.server.quiz.domain.model.Quiz;

public interface QuizRepository {
	<S extends Quiz> List<S> saveAll(Iterable<S> quizList);
}