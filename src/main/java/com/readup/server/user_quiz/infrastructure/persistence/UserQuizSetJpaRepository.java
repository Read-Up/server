package com.readup.server.user_quiz.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.readup.server.user_quiz.domain.model.UserQuizSet;

public interface UserQuizSetJpaRepository extends JpaRepository<UserQuizSet, Long> {
	Optional<UserQuizSet> findByQuizSetIdAndCreatedBy(Long quizSetId, Long userId);

	@Query("SELECT uqs FROM UserQuizSet uqs JOIN FETCH uqs.userQuizList uq WHERE uqs.quizSetId = :quizSetId AND uq.quizId = :quizId AND uqs.createdBy = :userId")
	Optional<UserQuizSet> findUserQuizSetWithUserQuiz(@Param("quizSetId") Long quizSetId, @Param("quizId") Long quizId,
		@Param("userId") Long userId);
}
