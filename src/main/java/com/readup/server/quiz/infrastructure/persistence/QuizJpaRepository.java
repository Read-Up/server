package com.readup.server.quiz.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.readup.server.quiz.domain.model.Quiz;
import com.readup.server.quiz.domain.repository.QuizRepository;

@Repository
public interface QuizJpaRepository extends JpaRepository<Quiz, Long>, QuizRepository {
}
