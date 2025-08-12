package com.readup.server.user_quiz.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.readup.server.user_quiz.domain.model.UserQuizSet;

public interface UserQuizSetJpaRepository extends JpaRepository<UserQuizSet, Long> {
	Optional<UserQuizSet> findByQuizSetIdAndCreatedBy(Long quizSetId, Long userId);

	Optional<UserQuizSet> findByIdAndCreatedBy(Long userQuizSetId, Long userId);

	@Query("SELECT uqs FROM UserQuizSet uqs JOIN FETCH uqs.userQuizList uq WHERE uqs.quizSetId = :quizSetId AND uqs.createdBy = :socialAccountId")
	Optional<UserQuizSet> findWithUserQuizByQuizSetId(Long quizSetId, Long socialAccountId);

	@Query("SELECT uqs FROM UserQuizSet uqs JOIN FETCH uqs.userQuizList uq WHERE uqs.id = :userQuizSetId AND uqs.createdBy = :socialAccountId")
	Optional<UserQuizSet> findWithUserQuizById(Long userQuizSetId, Long socialAccountId);

	@Query("SELECT uqs FROM UserQuizSet uqs JOIN FETCH uqs.userQuizList uq WHERE uqs.id = :userQuizSetId AND uqs.quizSetId = :quizSetId AND uqs.createdBy = :socialAccountId")
	Optional<UserQuizSet> findWithUserQuizByIdAndQuizSetId(Long userQuizSetId, Long quizSetId, Long socialAccountId);
}
