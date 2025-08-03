package com.readup.server.user.dto;

import com.readup.server.terms.domain.UserTermsConsent;
import com.readup.server.user.domain.User;
import com.readup.server.user.domain.UserStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record UserResponse(
	Long id,
	String nickname,
//	String email,
//	List<UserTermsConsent> userTermsConsentList,
	String imageUrl,
	UserStatus status,
	Long createdBy,
	LocalDateTime createdAt,
	Long updatedBy,
	LocalDateTime updatedAt,
	LocalDateTime deletedAt
) {
	public static UserResponse from(User user) {

		// TODO: 영속성 context 문제로 lazyloading 으로 가져오는 필드값은 못 가져오는 상황
		return new UserResponse(
				user.getId(),
				user.getNickname(),
//				user.getSocialAccount().getEmail(),
//				user.getUserTermsConsentList().stream().collect(Collectors.toList()),
				user.getImageUrl(),
				user.getStatus(),
				user.getCreatedBy(),
				user.getCreatedAt(),
				user.getUpdatedBy(),
				user.getUpdatedAt(),
				user.getDeletedAt()
		);
	}
}