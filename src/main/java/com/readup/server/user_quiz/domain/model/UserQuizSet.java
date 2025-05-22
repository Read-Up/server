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

	@Column(name = "quiz_sequence", nullable = false)
	private int quizSequence;

	@Column(name = "correct_answer_average")
	private Double correctAnswerAverage;

	@Column(name = "like_score")
	private Integer likeScore;

	@OneToMany(mappedBy = "userQuizSet", cascade = ALL, orphanRemoval = true)
	private List<UserQuiz> userQuizList = new ArrayList<>();

	private UserQuizSet(Long quizSetId) {
		this.quizSetId = quizSetId;
		this.isEvaluated = FALSE;
		this.quizSequence = 1;
	}

	public static UserQuizSet create(Long quizSetId, List<Long> quizIdList) {
		UserQuizSet userQuizSet = new UserQuizSet(quizSetId);
		userQuizSet.addUserQuizzes(quizIdList);
		return userQuizSet;
	}

	private void addUserQuizzes(List<Long> quizIdList) {
		if (hasQuizIds(quizIdList)) {
			quizIdList.forEach(this::addUserQuiz);
		}
	}

	private void addUserQuiz(Long quizId) {
		UserQuiz userQuiz = UserQuiz.create(quizId, this);
		userQuizList.add(userQuiz);
	}

	private boolean hasQuizIds(List<Long> quizIdList) {
		return quizIdList != null && !quizIdList.isEmpty();
	}
}