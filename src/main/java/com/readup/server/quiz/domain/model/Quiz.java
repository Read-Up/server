package com.readup.server.quiz.domain.model;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.readup.server.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE quiz SET deleted_at = NOW() WHERE id = ?")
@Table(name = "quiz")
@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Quiz extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "question", nullable = false, length = 150)
	private String question;

	@Column(name = "explanation")
	private String explanation;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "quiz_set_id", nullable = false)
	public QuizSet quizSet;

	@OneToMany(mappedBy = "quiz", cascade = ALL, orphanRemoval = true)
	private List<QuizOption> quizOptionList;

	@Builder(access = PRIVATE)
	private Quiz(String question, String explanation, QuizSet quizSet) {
		this.question = question;
		this.explanation = explanation;
		this.quizSet = quizSet;
		this.quizOptionList = new ArrayList<>();
	}

	public static Quiz create(String question, String explanation, QuizSet quizSet) {
		return Quiz.builder()
			.question(question)
			.explanation(explanation)
			.quizSet(quizSet)
			.build();
	}

	public void addQuizOptionList(List<QuizOption> quizOptionList) {
		if (quizOptionList == null || quizOptionList.isEmpty()) {
			return;
		}
		this.quizOptionList.addAll(quizOptionList);
	}

	public boolean isAnswerCorrect(Set<Long> selectedQuizOptionIds) {
		Set<Long> correctQuizOptionIds = quizOptionList.stream()
			.filter(QuizOption::getIsCorrect)
			.map(QuizOption::getId)
			.collect(Collectors.toUnmodifiableSet());
		return correctQuizOptionIds.equals(selectedQuizOptionIds);
	}
}
