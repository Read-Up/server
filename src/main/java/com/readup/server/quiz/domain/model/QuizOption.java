package com.readup.server.quiz.domain.model;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.readup.server.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE quiz_option SET deleted_at = NOW() WHERE id = ?")
@Table(name = "quiz_option")
@Getter
@Entity
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class QuizOption extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Column(nullable = false)
	private int sequence;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private Boolean isCorrect;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(nullable = false)
	private Quiz quiz;

	public static QuizOption create(int sequence, String content, Boolean isCorrect, Quiz quiz) {
		return QuizOption.builder()
			.sequence(sequence)
			.content(content)
			.isCorrect(isCorrect)
			.quiz(quiz)
			.build();
	}
}
