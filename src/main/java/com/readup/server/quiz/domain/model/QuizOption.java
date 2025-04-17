package com.readup.server.quiz.domain.model;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import com.readup.server.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class QuizOption extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(nullable = false)
	private Quiz quiz;

	@Column(nullable = false)
	private Integer number;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private Boolean isCorrect;

	public static QuizOption create(Quiz quiz, Integer number, String content, Boolean isCorrect) {
		return QuizOption.builder()
			.quiz(quiz)
			.number(number)
			.content(content)
			.isCorrect(isCorrect)
			.build();
	}
}
