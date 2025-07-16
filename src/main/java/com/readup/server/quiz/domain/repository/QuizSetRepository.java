package com.readup.server.quiz.domain.repository;

import org.springframework.data.domain.Pageable;

import com.readup.server.quiz.application.dto.GetQuizSetPageResponse;
import com.readup.server.quiz.application.dto.SliceResponse;
import com.readup.server.quiz.domain.model.QuizSet;

public interface QuizSetRepository {
	QuizSet save(QuizSet quizSet);

	QuizSet getQuizSetById(Long quizSetId);

	SliceResponse<GetQuizSetPageResponse> getAllQuizSets(Long bookId, Long chapterId, Pageable pageable);

	SliceResponse<GetQuizSetPageResponse> getMyQuizSets(Long socialAccountId, Long bookId, Long chapterId,
		Pageable pageable);

	SliceResponse<GetQuizSetPageResponse> getParticipatingQuizSets(Long socialAccountId, Long bookId, Long chapterId,
		Pageable pageable);
}