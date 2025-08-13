package com.readup.server.quiz.application;

import static com.readup.server.common.exception.ErrorCode.*;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readup.server.book.domain.repository.BookRepository;
import com.readup.server.common.exception.ServiceException;
import com.readup.server.quiz.application.dto.CreateQuizSetRequest;
import com.readup.server.quiz.application.dto.CreateQuizSetResponse;
import com.readup.server.quiz.application.dto.GetQuizSetPageResponse;
import com.readup.server.quiz.application.dto.GetQuizSetResponse;
import com.readup.server.quiz.application.dto.SliceResponse;
import com.readup.server.quiz.domain.model.QuizSet;
import com.readup.server.quiz.domain.repository.QuizSetRepository;
import com.readup.server.user_quiz.domain.model.UserQuizSetStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizSetService {

	private final BookRepository bookRepository;
	private final QuizSetRepository quizSetRepository;

	@Transactional
	public CreateQuizSetResponse createQuizSet(CreateQuizSetRequest request) {
		validateChapter(request.bookId(), request.chapterId());

		QuizSet savedQuizSet = quizSetRepository.save(request.toEntity());

		return CreateQuizSetResponse.from(request.bookId(), savedQuizSet);
	}

	@Transactional(readOnly = true)
	public GetQuizSetResponse getQuizSet(Long quizSetId, Long lastQuizId) {
		QuizSet quizSet = quizSetRepository.getQuizSetById(quizSetId);
		return GetQuizSetResponse.from(quizSet, lastQuizId);
	}

	@Transactional(readOnly = true)
	public SliceResponse<GetQuizSetPageResponse> getAllQuizSets(Long bookId, Long chapterId, Pageable pageable) {
		return quizSetRepository.getAllQuizSets(bookId, chapterId, pageable);
	}

	@Transactional(readOnly = true)
	public SliceResponse<GetQuizSetPageResponse> getMyQuizSets(Long socialAccountId, Long bookId, Long chapterId,
		Pageable pageable) {
		return quizSetRepository.getMyQuizSets(socialAccountId, bookId, chapterId, pageable);
	}

	@Transactional(readOnly = true)
	public SliceResponse<GetQuizSetPageResponse> getParticipatingQuizSets(Long socialAccountId, Long bookId,
		Long chapterId, String userQuizSetStatus, Pageable pageable) {
		return quizSetRepository.getParticipatingQuizSets(socialAccountId, bookId, chapterId,
			UserQuizSetStatus.fromString(userQuizSetStatus), pageable);
	}

	private void validateChapter(Long bookId, Long chapterId) {
		bookRepository.getBookById(bookId).getChapterList().stream()
			.filter(c -> c.getId().equals(chapterId))
			.findFirst()
			.orElseThrow(() -> new ServiceException(NOT_FOUND_CHAPTER));
	}
}
