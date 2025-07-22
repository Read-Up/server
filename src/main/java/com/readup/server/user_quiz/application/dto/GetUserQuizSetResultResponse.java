package com.readup.server.user_quiz.application.dto;

public record GetUserQuizSetResultResponse(int solvedQuizCount, int firstAttemptCorrectCount, int retryCorrectCount,
										   boolean isAboveHalfCorrect) {
}