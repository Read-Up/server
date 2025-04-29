package com.readup.server.quiz.domain.model;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE quiz SET deleted_at = NOW() WHERE id = ?")
@Table(name = "quiz")
@Getter
@Entity
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class Quiz extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Column(nullable = false, length = 150)
	private String question;

	@Column
	private String explanation;

	@ManyToOne(fetch = LAZY)
	public QuizSet quizSet;

	@OneToMany(mappedBy = "quiz", cascade = ALL, orphanRemoval = true)
	private List<QuizOption> quizOptionList;

	public static Quiz create(String question, String explanation, QuizSet quizSet) {
		return Quiz.builder()
			.question(question)
			.explanation(explanation)
			.quizSet(quizSet)
			.quizOptionList(new ArrayList<>())
			.build();
	}

	public void addQuizOption(String content, Boolean isCorrect) {
		QuizOption quizOption = QuizOption.create(content, isCorrect, this);
		quizOptionList.add(quizOption);
	}
}
