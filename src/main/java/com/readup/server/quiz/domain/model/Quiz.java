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
import com.readup.server.quiz.application.dto.CreateQuizSetRequest.CreateQuizRequest.CreateQuizOptionRequest;

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

	@Column(name = "sequence", nullable = false)
	private int sequence;

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
	private Quiz(int sequence, String question, String explanation, QuizSet quizSet) {
		this.sequence = sequence;
		this.question = question;
		this.explanation = explanation;
		this.quizSet = quizSet;
		this.quizOptionList = new ArrayList<>();
	}

	public static Quiz create(int sequence, String question, String explanation, QuizSet quizSet,
		List<CreateQuizOptionRequest> createQuizOptionRequestList) {
		return Quiz.builder()
			.sequence(sequence)
			.question(question)
			.explanation(explanation)
			.quizSet(quizSet)
			.build()
			.addQuizOptions(createQuizOptionRequestList);
	}

	private Quiz addQuizOptions(List<CreateQuizOptionRequest> createQuizOptionRequestList) {
		if (hasQuizOptionRequests(createQuizOptionRequestList)) {
			createQuizOptionRequestList.forEach(cqor -> this.addQuizOption(cqor.content(), cqor.isCorrect()));
		}
		return this;
	}

	private void addQuizOption(String content, Boolean isCorrect) {
		int nextSequence = quizOptionList.size() + 1;
		QuizOption quizOption = QuizOption.create(nextSequence, content, isCorrect, this);
		quizOptionList.add(quizOption);
	}

	private boolean hasQuizOptionRequests(List<CreateQuizOptionRequest> createQuizOptionRequestList) {
		return createQuizOptionRequestList != null && !createQuizOptionRequestList.isEmpty();
	}

	public boolean isAnswerCorrect(Set<Integer> selectedQuizOptionSequences) {
		Set<Integer> correctQuizOptionSequences = quizOptionList.stream()
			.filter(QuizOption::getIsCorrect)
			.map(QuizOption::getSequence)
			.collect(Collectors.toUnmodifiableSet());
		return correctQuizOptionSequences.equals(selectedQuizOptionSequences);
	}
}
