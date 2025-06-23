package com.readup.server.quiz.domain.model;

import static com.readup.server.quiz.application.QuizSetTimeCalculator.*;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE quiz_set SET deleted_at = NOW() WHERE id = ?")
@Table(name = "quiz_set")
@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class QuizSet extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "book_id", nullable = false)
	private Long bookId;

	@Column(name = "chapter_id", nullable = false)
	private Long chapterId;

	@Column(name = "participant_count", nullable = false)
	private int participantCount;

	@Column(name = "like_average", nullable = false)
	private double likeAverage;

	@Column(name = "correct_answer_average", nullable = false)
	private double correctAnswerAverage;

	@Column(name = "estimated_time", nullable = false)
	private int estimatedTime;

	@Column(name = "total_quiz_count", nullable = false)
	private int totalQuizCount;

	@OneToMany(mappedBy = "quizSet", cascade = ALL, orphanRemoval = true)
	private List<Quiz> quizList = new ArrayList<>();

	@Builder(access = PRIVATE)
	private QuizSet(Long bookId, Long chapterId) {
		this.bookId = bookId;
		this.chapterId = chapterId;
	}

	public static QuizSet create(Long bookId, Long chapterId) {
		return QuizSet.builder()
			.bookId(bookId)
			.chapterId(chapterId)
			.build();
	}

	public void addQuizList(List<Quiz> quizList) {
		if (this.quizList == null || quizList.isEmpty()) {
			return;
		}
		this.quizList.addAll(quizList);
		updateTotalQuizCount();
		updateEstimatedTime(calculate(totalQuizCount));
	}

	public void updateTotalQuizCount() {
		this.totalQuizCount = this.quizList.size();
	}

	public void updateEstimatedTime(int estimatedTime) {
		this.estimatedTime = estimatedTime;
	}
}
