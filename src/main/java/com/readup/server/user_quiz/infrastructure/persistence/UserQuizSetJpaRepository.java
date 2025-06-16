package com.readup.server.user_quiz.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readup.server.user_quiz.domain.model.UserQuizSet;
import com.readup.server.user_quiz.domain.repository.UserQuizSetRepository;

public interface UserQuizSetJpaRepository extends UserQuizSetRepository, JpaRepository<UserQuizSet, Long> {
	Optional<UserQuizSet> findByQuizSetIdAndCreatedBy(Long quizSetId, Long userId);
}
