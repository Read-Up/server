package com.readup.server.quiz.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readup.server.quiz.domain.model.QuizSet;
import com.readup.server.quiz.domain.repository.QuizSetRepository;

public interface QuizSetJpaRepository extends QuizSetRepository, JpaRepository<QuizSet, Long> {
}
