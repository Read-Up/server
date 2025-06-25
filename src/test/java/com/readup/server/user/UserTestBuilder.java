package com.readup.server.user;

import com.readup.server.user.domain.User;
import com.readup.server.user.domain.UserStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;

public class UserTestBuilder {
	private Long id = 1L;
	private String nickname = "테스트 유저";

	public static UserTestBuilder builder() {
		return new UserTestBuilder();
	}

	public UserTestBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public UserTestBuilder nickname(String nickname) {
		this.nickname = nickname;
		return this;
	}

	public User build() {
		User user = User.register(nickname, null);
		ReflectionTestUtils.setField(user, "id", id);
		return user;
	}
}
