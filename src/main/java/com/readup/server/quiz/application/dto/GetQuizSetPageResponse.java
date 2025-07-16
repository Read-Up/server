package com.readup.server.quiz.application.dto;

import java.time.LocalDateTime;

public record GetQuizSetPageResponse(String nickname, Long quizSetId, int totalQuizCount, int participantCount,
									 double likeAverage, double correctAnswerAverage, int estimatedTime,
									 LocalDateTime createdAt) {
}