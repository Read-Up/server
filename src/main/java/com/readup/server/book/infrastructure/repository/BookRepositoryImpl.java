package com.readup.server.book.infrastructure.repository;

import static com.readup.server.book.domain.QBook.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.readup.server.book.application.dto.GetBookResponse;
import com.readup.server.book.domain.Book;
import com.readup.server.book.domain.repository.BookRepository;
import com.readup.server.common.exception.ErrorCode;
import com.readup.server.common.exception.RepositoryException;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {

	private final JPAQueryFactory jpaQueryFactory;
	private final BookJpaRepository bookJpaRepository;

	@Override
	public Book save(Book newBook) {
		return bookJpaRepository.save(newBook);
	}

	@Override
	public boolean existsByIsbn(String isbn) {
		return bookJpaRepository.existsByIsbn(isbn);
	}

	@Override
	public Book getBookById(Long id) {
		return bookJpaRepository.findBookById(id)
			.orElseThrow(() -> new RepositoryException(ErrorCode.BOOK_NOT_FOUND));
	}

	@Override
	public Page<GetBookResponse> searchBook(String title, String isbn, Pageable pageable) {
		List<Book> bookList = getBookListByTitleOrIsbn(title, isbn, pageable);
		List<GetBookResponse> getBookResponseList = bookList.stream()
			.map(GetBookResponse::from)
			.toList();

		JPAQuery<Long> countQuery = getCountQueryByTitleOrIsbn(title, isbn);

		return PageableExecutionUtils.getPage(getBookResponseList, pageable, countQuery::fetchOne);
	}

	private JPAQuery<Long> getCountQueryByTitleOrIsbn(String title, String isbn) {
		return jpaQueryFactory.select(book.count())
			.from(book)
			.where(searchBookByTitleOrIsbnPredicate(title, isbn));
	}

	private List<Book> getBookListByTitleOrIsbn(String title, String isbn, Pageable pageable) {
		return jpaQueryFactory.select(book)
			.from(book)
			.where(searchBookByTitleOrIsbnPredicate(title, isbn))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	private Predicate searchBookByTitleOrIsbnPredicate(String title, String isbn) {
		return ExpressionUtils.allOf(
			title != null ? book.title.contains(title) : null,
			isbn != null ? book.isbn.contains(isbn) : null
		);
	}
}
