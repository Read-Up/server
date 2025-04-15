package com.readup.server.book.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readup.server.book.application.client.BookInfoClientRegistry;
import com.readup.server.book.application.client.vo.BookInfoVO;
import com.readup.server.book.domain.Book;
import com.readup.server.book.domain.repository.BookRepository;
import com.readup.server.book.presentation.dto.GetExternalBookResponse;
import com.readup.server.common.exception.ErrorCode;
import com.readup.server.common.exception.ServiceException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookInfoService {
	private final BookInfoClientRegistry bookInfoClientRegistry;
	private final BookRepository bookRepository;

	@Transactional
	public GetExternalBookResponse getBookInfo(String isbn) {

		BookInfoVO bookInfoVO = bookInfoClientRegistry.getDefaultBookInfoClient().getBookInfo(isbn);

		if (bookRepository.existsByIsbnOrTitle(bookInfoVO.isbn(), bookInfoVO.bookTitle())) {
			throw new ServiceException(ErrorCode.DUPLICATE_BOOK);
		}
		Book newBook = bookInfoVO.toEntity();
		Book savedBook = bookRepository.save(newBook);
		return GetExternalBookResponse.from(savedBook);
	}
}
