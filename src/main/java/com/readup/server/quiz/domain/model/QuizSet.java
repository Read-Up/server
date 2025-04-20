package com.readup.server.quiz.domain.model;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;

import com.readup.server.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "quiz_set")
@Getter
@Entity
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class QuizSet extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long chapterId;

	@OneToMany(mappedBy = "quizSet", cascade = ALL, orphanRemoval = true)
	private List<Quiz> quizList;

	public static QuizSet create(Long chapterId) {
		return QuizSet.builder()
			.chapterId(chapterId)
			.quizList(new ArrayList<>())
			.build();
	}

	public Quiz addQuiz(String question, String explanation) {
		Quiz quiz = Quiz.create(question, explanation, this);
		quizList.add(quiz);
		return quiz;
	}
}
