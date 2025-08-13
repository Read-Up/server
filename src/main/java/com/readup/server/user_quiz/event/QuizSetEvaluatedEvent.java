package com.readup.server.user_quiz.event;

public record QuizSetEvaluatedEvent(Long quizSetId, Long userQuizSetId, Double correctAnswerAverage, Integer likeScore) {
}
