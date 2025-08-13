package com.readup.server.quiz.infrastructure.persistence;

import static com.readup.server.auth.domain.QSocialAccount.*;
import static com.readup.server.common.exception.ErrorCode.*;
import static com.readup.server.quiz.domain.model.QQuizSet.*;
import static com.readup.server.quiz.infrastructure.persistence.sort.QuizSetSortProvider.*;
import static com.readup.server.user.domain.QUser.*;
import static com.readup.server.user_quiz.domain.model.QUserQuizSet.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.readup.server.common.exception.RepositoryException;
import com.readup.server.quiz.application.dto.GetQuizSetPageResponse;
import com.readup.server.quiz.application.dto.SliceResponse;
import com.readup.server.quiz.domain.model.QuizSet;
import com.readup.server.quiz.domain.repository.QuizSetRepository;
import com.readup.server.user_quiz.domain.model.UserQuizSetStatus;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QuizSetRepositoryImpl implements QuizSetRepository {

	private static final String WITHDRAWN_NICKNAME = "알 수 없음";
	private static final String WITHDRAWN_IMAGE_URL = "탈퇴한 유저 프로필 이미지 URL 필요";

	private final JPAQueryFactory jpaQueryFactory;
	private final QuizSetJpaRepository quizSetJpaRepository;

	@Override
	public QuizSet save(QuizSet quizSet) {
		return quizSetJpaRepository.save(quizSet);
	}

	@Override
	public QuizSet getQuizSetById(Long quizSetId) {
		return quizSetJpaRepository.findById(quizSetId)
			.orElseThrow(() -> new RepositoryException(NOT_FOUND_QUIZ_SET));
	}

	@Override
	public SliceResponse<GetQuizSetPageResponse> getAllQuizSets(Long bookId, Long chapterId, Pageable pageable) {
		List<GetQuizSetPageResponse> content = createQuizSetPageBaseQuery(pageable)
			.where(bookIdEq(bookId), chapterIdEq(chapterId))
			.fetch();
		return createQuizSetSliceResponse(content, pageable);
	}

	@Override
	public SliceResponse<GetQuizSetPageResponse> getMyQuizSets(Long socialAccountId, Long bookId, Long chapterId,
		Pageable pageable) {
		List<GetQuizSetPageResponse> content = createQuizSetPageBaseQuery(pageable)
			.where(quizSetCreatedByEq(socialAccountId), bookIdEq(bookId), chapterIdEq(chapterId))
			.fetch();
		return createQuizSetSliceResponse(content, pageable);
	}

	@Override
	public SliceResponse<GetQuizSetPageResponse> getParticipatingQuizSets(Long socialAccountId, Long bookId,
		Long chapterId, UserQuizSetStatus userQuizSetStatus, Pageable pageable) {
		List<GetQuizSetPageResponse> content = jpaQueryFactory
			.select(createQuizSetProjection())
			.from(quizSet)
			.innerJoin(userQuizSet)
			.on(userQuizSet.quizSetId.eq(quizSet.id)
				.and(userQuizSetCreatedByEq(socialAccountId))
				.and(userQuizSetStatusEq(userQuizSetStatus)))
			.leftJoin(socialAccount).on(socialAccount.id.eq(quizSet.createdBy))
			.leftJoin(socialAccount.user, user)
			.where(bookIdEq(bookId), chapterIdEq(chapterId))
			.orderBy(getParticipatingOrderSpecifiers(pageable))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1L)
			.fetch();
		return createQuizSetSliceResponse(content, pageable);
	}

	private JPAQuery<GetQuizSetPageResponse> createQuizSetPageBaseQuery(Pageable pageable) {
		return jpaQueryFactory
			.select(createQuizSetProjection())
			.from(quizSet)
			.leftJoin(socialAccount).on(socialAccount.id.eq(quizSet.createdBy))
			.leftJoin(socialAccount.user, user)
			.orderBy(getOrderSpecifiers(pageable))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1L);
	}

	private SliceResponse<GetQuizSetPageResponse> createQuizSetSliceResponse(List<GetQuizSetPageResponse> content,
		Pageable pageable) {
		boolean hasNext = hasNextPage(content, pageable);
		List<GetQuizSetPageResponse> pageContent = getPageContent(content, hasNext);
		return SliceResponse.from(new SliceImpl<>(pageContent, pageable, hasNext));
	}

	private ConstructorExpression<GetQuizSetPageResponse> createQuizSetProjection() {
		return Projections.constructor(GetQuizSetPageResponse.class,
			user.id,
			user.nickname.coalesce(WITHDRAWN_NICKNAME),
			user.imageUrl.coalesce(WITHDRAWN_IMAGE_URL),
			quizSet.id,
			quizSet.totalQuizCount,
			quizSet.participantCount,
			quizSet.likeAverage,
			quizSet.correctAnswerAverage,
			quizSet.estimatedTime,
			quizSet.createdAt);
	}

	private BooleanExpression quizSetCreatedByEq(Long socialAccountId) {
		return socialAccountId != null ? quizSet.createdBy.eq(socialAccountId) : null;
	}

	private static BooleanExpression userQuizSetCreatedByEq(Long socialAccountId) {
		return socialAccountId != null ? userQuizSet.createdBy.eq(socialAccountId) : null;
	}

	private BooleanExpression bookIdEq(Long bookId) {
		return bookId != null ? quizSet.bookId.eq(bookId) : null;
	}

	private BooleanExpression chapterIdEq(Long chapterId) {
		return chapterId != null ? quizSet.chapterId.eq(chapterId) : null;
	}

	private static BooleanExpression userQuizSetStatusEq(UserQuizSetStatus userQuizSetStatus) {
		return userQuizSetStatus != null ? userQuizSet.status.eq(userQuizSetStatus) : null;
	}

	private boolean hasNextPage(List<GetQuizSetPageResponse> content, Pageable pageable) {
		return content.size() > pageable.getPageSize();
	}

	private List<GetQuizSetPageResponse> getPageContent(List<GetQuizSetPageResponse> content, boolean hasNext) {
		if (hasNext) {
			return new ArrayList<>(content.subList(0, content.size() - 1));
		}
		return content;
	}
}
