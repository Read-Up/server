package com.readup.server.quiz.application;

import static com.readup.server.common.exception.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readup.server.book.domain.Chapter;
import com.readup.server.book.domain.repository.BookRepository;
import com.readup.server.common.exception.ServiceException;
import com.readup.server.quiz.domain.model.Quiz;
import com.readup.server.quiz.domain.repository.QuizRepository;
import com.readup.server.quiz.application.dto.CreateQuizListRequest;
import com.readup.server.quiz.application.dto.CreateQuizListResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizService {

	private final BookRepository bookRepository;
	private final QuizRepository quizRepository;

	@Transactional
	public CreateQuizListResponse createQuiz(CreateQuizListRequest request) {

		Chapter chapter = getChapter(request.bookId(), request.chapterId());

		List<Quiz> quizList = request.quizRequestList().stream()
			.map(quizRequest -> quizRequest.toEntity(chapter))
			.toList();

		List<Quiz> savedQuizList = quizRepository.saveAll(quizList);

		return CreateQuizListResponse.from(request.bookId(), request.chapterId(), savedQuizList);
	}

	private Chapter getChapter(Long bookId, Long chapterId) {
		return bookRepository.getBookById(bookId).getChapterList().stream()
			.filter(c -> c.getId().equals(chapterId))
			.findFirst()
			.orElseThrow(() -> new ServiceException(NOT_FOUND_CHAPTER));
	}
}
