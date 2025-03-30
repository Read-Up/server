package com.readup.server.quiz.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readup.server.quiz.domain.model.Quiz;
import com.readup.server.quiz.domain.model.vo.QuizInfo;
import com.readup.server.quiz.domain.repository.QuizRepository;
import com.readup.server.quiz.presentation.dto.CreateQuizRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizService {

	private final QuizRepository quizRepository;

	@Transactional
	public Long createQuiz(CreateQuizRequest request) {
		QuizInfo quizInfo = request.toValueObject();
		Quiz quiz = quizInfo.toEntity();

		return quizRepository.save(quiz).getId();
	}

}
