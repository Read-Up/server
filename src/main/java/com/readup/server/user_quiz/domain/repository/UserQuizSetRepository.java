package com.readup.server.user_quiz.domain.repository;

import java.util.Optional;

import com.readup.server.user_quiz.domain.model.UserQuizSet;

public interface UserQuizSetRepository {
	UserQuizSet save(UserQuizSet userQuizSet);

	Optional<UserQuizSet> findByQuizSetIdAndCreatedBy(Long quizSetId, Long socialAccountId);

	UserQuizSet getByIdAndCreatedBy(Long userQuizSetId, Long socialAccountId);

	UserQuizSet getWithUserQuizByQuizSetId(Long quizSetId, Long socialAccountId);

	UserQuizSet getWithUserQuizById(Long userQuizSetId, Long socialAccountId);

	UserQuizSet getWithUserQuizByIdAndQuizSetId(Long userQuizSetId, Long quizSetId, Long socialAccountId);
}