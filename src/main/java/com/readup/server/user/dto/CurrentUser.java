package com.readup.server.user.dto;

import com.readup.server.user.domain.User;

public record CurrentUser(
	Long id,
	String nickname
) {
	public static CurrentUser from(User user) {
		return new CurrentUser(user.getId(), user.getNickname());
	}
}
