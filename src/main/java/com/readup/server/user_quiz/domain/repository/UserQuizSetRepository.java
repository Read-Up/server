package com.readup.server.user_quiz.domain.repository;

import java.util.Optional;

import com.readup.server.user_quiz.domain.model.UserQuizSet;

public interface UserQuizSetRepository {
	UserQuizSet save(UserQuizSet userQuizSet);

	Optional<UserQuizSet> findByQuizSetIdAndCreatedBy(Long quizSetId, Long socialAccountId);

	UserQuizSet getByIdAndCreatedBy(Long userQuizSetId, Long socialAccountId);

	UserQuizSet getUserQuizSetWithUserQuizByQuizSetId(Long quizSetId, Long quizId, Long socialAccountId);

	UserQuizSet getUserQuizSetWithUserQuizById(Long userQuizSetId, Long socialAccountId);
}