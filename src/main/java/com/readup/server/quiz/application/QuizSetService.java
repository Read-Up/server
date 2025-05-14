package com.readup.server.quiz.application;

import static com.readup.server.common.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readup.server.book.domain.repository.BookRepository;
import com.readup.server.common.exception.ServiceException;
import com.readup.server.quiz.application.dto.CreateQuizSetRequest;
import com.readup.server.quiz.application.dto.CreateQuizSetResponse;
import com.readup.server.quiz.application.dto.GetQuizSetResponse;
import com.readup.server.quiz.domain.model.QuizSet;
import com.readup.server.quiz.domain.repository.QuizSetRepository;

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
	public GetQuizSetResponse getQuizSet(Long quizSetId, int startQuizSequence) {
		QuizSet quizSet = quizSetRepository.findById(quizSetId)
			.orElseThrow(() -> new ServiceException(NOT_FOUND_QUIZ_SET));

		return GetQuizSetResponse.from(quizSet, startQuizSequence);
	}

	private void validateChapter(Long bookId, Long chapterId) {
		bookRepository.getBookById(bookId).getChapterList().stream()
			.filter(c -> c.getId().equals(chapterId))
			.findFirst()
			.orElseThrow(() -> new ServiceException(NOT_FOUND_CHAPTER));
	}
}
