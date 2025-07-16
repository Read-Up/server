package com.readup.server.user_quiz.domain.model;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.GenerationType.*;
import static java.lang.Boolean.*;
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
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user_quiz_set", uniqueConstraints = @UniqueConstraint(columnNames = {"quiz_set_id", "created_by"}))
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE user_quiz_set SET deleted_at = NOW() WHERE id = ?")
@NoArgsConstructor(access = PROTECTED)
public class UserQuizSet extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "quiz_set_id", nullable = false)
	private Long quizSetId;

	@Column(name = "is_evaluated", nullable = false)
	private Boolean isEvaluated;

	@Column(name = "last_quiz_id")
	private Long lastQuizId;

	@Column(name = "correct_answer_average")
	private Double correctAnswerAverage;

	@Column(name = "like_score")
	private Integer likeScore;

	@OneToMany(mappedBy = "userQuizSet", cascade = ALL, orphanRemoval = true)
	private List<UserQuiz> userQuizList = new ArrayList<>();

	@Column(name = "progress", nullable = false)
	private int progress;

	private UserQuizSet(Long quizSetId) {
		this.quizSetId = quizSetId;
		this.isEvaluated = FALSE;
		this.progress = 0;
	}

	public static UserQuizSet create(Long quizSetId) {
		return new UserQuizSet(quizSetId);
	}

	public void addUserQuizList(List<UserQuiz> userQuizList) {
		if (userQuizList == null || userQuizList.isEmpty()) {
			return;
		}
		this.userQuizList.addAll(userQuizList);
	}
}