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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE quiz_option SET deleted_at = NOW() WHERE id = ?")
@Table(name = "quiz_option")
@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class QuizOption extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "sequence", nullable = false)
	private int sequence;

	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "is_correct", nullable = false)
	private Boolean isCorrect;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "quiz_id", nullable = false)
	private Quiz quiz;

	@Builder(access = PRIVATE)
	private QuizOption(int sequence, String content, Boolean isCorrect, Quiz quiz) {
		this.sequence = sequence;
		this.content = content;
		this.isCorrect = isCorrect;
		this.quiz = quiz;
	}

	public static QuizOption create(int sequence, String content, Boolean isCorrect, Quiz quiz) {
		return QuizOption.builder()
			.sequence(sequence)
			.content(content)
			.isCorrect(isCorrect)
			.quiz(quiz)
			.build();
	}
}
