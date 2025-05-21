package com.readup.server.user_quiz.domain.model;

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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@Table(name = "user_quiz_set", uniqueConstraints = @UniqueConstraint(columnNames = {"quiz_set_id", "created_by"}))
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE user_quiz_set SET deleted_at = NOW() WHERE id = ?")
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class UserQuizSet extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(nullable = false)
	private Long quizSetId;

	@Column(nullable = false)
	private Boolean isEvaluated;

	@Column(nullable = false)
	private int quizSequence;

	@Column
	private Double correctAnswerAverage;

	@Column
	private Integer likeScore;

	@OneToMany(mappedBy = "userQuizSet", cascade = ALL, orphanRemoval = true)
	private List<UserQuiz> userQuizList;

	public static UserQuizSet create(Long quizSetId) {
		return UserQuizSet.builder()
			.quizSetId(quizSetId)
			.isEvaluated(false)
			.quizSequence(1)
			.userQuizList(new ArrayList<>())
			.build();
	}

	public void addUserQuiz(Long quizId) {
		UserQuiz userQuiz = UserQuiz.create(quizId, this);
		userQuizList.add(userQuiz);
	}
}
