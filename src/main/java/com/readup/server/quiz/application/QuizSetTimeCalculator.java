package com.readup.server.quiz.application;

public class QuizSetTimeCalculator {

	private QuizSetTimeCalculator() {
	}

	public static int calculate(int totalQuizCount) {
		return totalQuizCount * 2;
	}
}
