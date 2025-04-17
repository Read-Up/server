package com.readup.server.quiz.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readup.server.quiz.domain.model.Quiz;
import com.readup.server.quiz.domain.repository.QuizRepository;

public interface QuizJpaRepository extends QuizRepository, JpaRepository<Quiz, Long> {
}
