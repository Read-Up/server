package com.readup.server.user.dto;

import com.readup.server.user.domain.User;

public record AuthUser(
	Long id,
	String nickname
) {
	public static AuthUser from(User user) {
		return new AuthUser(user.getId(), user.getNickname());
	}
}
