package com.readup.server.quiz.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readup.server.quiz.domain.model.QuizSet;

public interface QuizSetJpaRepository extends JpaRepository<QuizSet, Long> {
}
