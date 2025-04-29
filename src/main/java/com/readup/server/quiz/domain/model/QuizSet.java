package com.readup.server.quiz.domain.model;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

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

@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE quiz_set SET deleted_at = NOW() WHERE id = ?")
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
	private Long bookId;

	@Column(nullable = false)
	private Long chapterId;

	@Column(nullable = false)
	private int participantCount;

	@Column(nullable = false)
	private double likeAverage;

	@Column(nullable = false)
	private double correctAnswerAverage;

	@OneToMany(mappedBy = "quizSet", cascade = ALL, orphanRemoval = true)
	private List<Quiz> quizList;

	public static QuizSet create(Long bookId, Long chapterId) {
		return QuizSet.builder()
			.bookId(bookId)
			.chapterId(chapterId)
			.participantCount(0)
			.likeAverage(0.0)
			.correctAnswerAverage(0.0)
			.quizList(new ArrayList<>())
			.build();
	}

	public Quiz addQuiz(String question, String explanation) {
		Quiz quiz = Quiz.create(question, explanation, this);
		quizList.add(quiz);
		return quiz;
	}
}
