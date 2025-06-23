package com.readup.server.user_quiz.application.dto;

import java.util.Set;

public record SubmitUserQuizRequest(Set<Long> selectedQuizOptionIds) {
}