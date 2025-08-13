package com.readup.server.user_quiz.application.dto;

import org.hibernate.validator.constraints.Range;

public record EvaluateQuizSetRequest(Long userQuizSetId, @Range(min = 1, max = 5) int likeScore) {
}
