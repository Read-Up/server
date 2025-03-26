package com.readup.server.book.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readup.server.book.domain.Book;
import com.readup.server.book.infrastructure.client.bookinfo.BookInfoClientRegistry;
import com.readup.server.book.infrastructure.client.bookinfo.vo.BookInfoVO;
import com.readup.server.book.infrastructure.repository.BookJpaRepository;
import com.readup.server.book.presentation.dto.GetExternalBookResponse;
import com.readup.server.common.exception.ErrorCode;
import com.readup.server.common.exception.ServiceException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookInfoService {
	private final BookInfoClientRegistry bookInfoClientRegistry;
	private final BookJpaRepository bookJpaRepository;

	@Transactional
	public GetExternalBookResponse getBookInfo(String isbn) {

		BookInfoVO bookInfoVO = bookInfoClientRegistry.getDefaultBookInfoClient().getBookInfo(isbn);

		if (bookJpaRepository.existsByIsbnOrTitle(bookInfoVO.isbn(), bookInfoVO.bookTitle())) {
			throw new ServiceException(ErrorCode.DUPLICATE_BOOK, "Book already exists");
		}
		Book newBook = bookInfoVO.toEntity();
		Book savedBook = bookJpaRepository.save(newBook);
		return GetExternalBookResponse.from(savedBook);
	}
}
