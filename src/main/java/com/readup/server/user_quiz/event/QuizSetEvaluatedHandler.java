package com.readup.server.user_quiz.event;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.readup.server.quiz.application.QuizSetEvaluationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class QuizSetEvaluatedHandler {

	private final QuizSetEvaluationService quizSetEvaluationService;

	@Async
	@TransactionalEventListener
	public void handle(QuizSetEvaluatedEvent event) {
		quizSetEvaluationService.applyUserEvaluation(event);
	}
}
