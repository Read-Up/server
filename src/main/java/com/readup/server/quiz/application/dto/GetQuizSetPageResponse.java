package com.readup.server.quiz.application.dto;

import java.time.LocalDateTime;

public record GetQuizSetPageResponse(Long userId, String nickname, String profileImageUrl, Long quizSetId,
									 int totalQuizCount, int participantCount, double likeAverage,
									 double correctAnswerAverage, int estimatedTime, LocalDateTime createdAt) {
}