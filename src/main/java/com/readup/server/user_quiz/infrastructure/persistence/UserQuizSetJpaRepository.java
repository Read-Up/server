package com.readup.server.user_quiz.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.readup.server.user_quiz.domain.model.UserQuizSet;

public interface UserQuizSetJpaRepository extends JpaRepository<UserQuizSet, Long> {
	Optional<UserQuizSet> findByQuizSetIdAndCreatedBy(Long quizSetId, Long userId);

	Optional<UserQuizSet> findByIdAndCreatedBy(Long userQuizSetId, Long userId);

	@Query("SELECT uqs FROM UserQuizSet uqs JOIN FETCH uqs.userQuizList uq WHERE uqs.quizSetId = :quizSetId AND uq.quizId = :quizId AND uqs.createdBy = :socialAccountId")
	Optional<UserQuizSet> findUserQuizSetWithUserQuiz(@Param("quizSetId") Long quizSetId, @Param("quizId") Long quizId,
		@Param("socialAccountId") Long socialAccountId);

	@Query("SELECT uqs FROM UserQuizSet uqs JOIN FETCH uqs.userQuizList uq WHERE uqs.id = :userQuizSetId AND uqs.createdBy = :socialAccountId")
	Optional<UserQuizSet> findUserQuizSetWithUserQuiz(@Param("userQuizSetId") Long userQuizSetId,
		@Param("socialAccountId") Long socialAccountId);
}
