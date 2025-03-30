package com.readup.server.quiz.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.readup.server.quiz.domain.model.QuizOption;
import com.readup.server.quiz.domain.repository.QuizOptionRepository;

@Repository
public interface QuizOptionJpaRepository extends JpaRepository<QuizOption, Long>, QuizOptionRepository {
}
