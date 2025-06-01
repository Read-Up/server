package com.readup.server.user_quiz.domain.repository;

import java.util.Optional;

import com.readup.server.user_quiz.domain.model.UserQuizSet;

public interface UserQuizSetRepository {
	UserQuizSet save(UserQuizSet userQuizSet);

	Optional<UserQuizSet> findByQuizSetIdAndCreatedBy(Long quizSetId, Long userId);

	UserQuizSet getUserQuizSetWithUserQuizById(Long quizSetId, Long quizId, Long userId);
}