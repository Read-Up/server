package com.readup.server.user_quiz.domain.model;

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
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user_quiz", uniqueConstraints = @UniqueConstraint(columnNames = {"quiz_id", "created_by"}))
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE user_quiz SET deleted_at = NOW() WHERE id = ?")
@NoArgsConstructor(access = PROTECTED)
public class UserQuiz extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "quiz_id", nullable = false)
	private Long quizId;

	@Column(name = "attempt_count", nullable = false)
	private int attemptCount;

	@Column(name = "is_correct")
	private Boolean isCorrect;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_quiz_set_id", nullable = false)
	private UserQuizSet userQuizSet;

	private UserQuiz(Long quizId, UserQuizSet userQuizSet) {
		this.quizId = quizId;
		this.userQuizSet = userQuizSet;
	}

	public static UserQuiz create(Long quizId, UserQuizSet userQuizSet) {
		return new UserQuiz(quizId, userQuizSet);
	}

	public void submitUserQuiz(Boolean isCorrect) {
		this.attemptCount++;
		this.isCorrect = isCorrect;
	}
}
