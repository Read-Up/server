package com.readup.server.quiz.domain.model;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;

import com.readup.server.book.domain.Chapter;
import com.readup.server.common.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class Quiz extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(nullable = false)
	private Chapter chapter;

	@Column(nullable = false, length = 150)
	private String question;

	@Column
	private String explanation;

	@Column(nullable = false)
	private Integer answerOptionNumber;

	@OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<QuizOption> quizOptionList;

	public static Quiz create(Chapter chapter, String question, String explanation) {
		return Quiz.builder()
			.chapter(chapter)
			.question(question)
			.explanation(explanation)
			.quizOptionList(new ArrayList<>())
			.build();
	}

	public void addAnswerOptionNumber(Integer answerOptionNumber) {
		this.answerOptionNumber = answerOptionNumber;
	}

	public void addQuizOption(Integer number, String content, Boolean isCorrect) {
		QuizOption quizOption = QuizOption.create(this, number, content, isCorrect);
		quizOptionList.add(quizOption);
	}
}
