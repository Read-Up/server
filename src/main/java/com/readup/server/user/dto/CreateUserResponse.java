package com.readup.server.user.dto;

import com.readup.server.user.domain.User;

public record CreateUserResponse(
	Long id,
	String nickname
) {
	public static CreateUserResponse from(User user) {
		return new CreateUserResponse(user.getId(), user.getNickname());
	}
}
