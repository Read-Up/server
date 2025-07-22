package com.readup.server.user;

import com.readup.server.auth.domain.SocialAccount;
import com.readup.server.user.domain.User;
import com.readup.server.user.domain.UserStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;

public class UserTestBuilder {
	private Long id = 1L;
	private String nickname = "테스트 유저";
	private SocialAccount socialAccount = SocialAccount.builder()
			.email("test@email.com")
			.provider("KAKAO")
			.providerUid("12345678")
			.build();

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

	public UserTestBuilder socialAccount(SocialAccount socialAccount) {
		this.socialAccount = socialAccount;
		return this;
	}

	public User build() {
		User user = User.register(nickname, socialAccount);
		ReflectionTestUtils.setField(user, "id", id);
		return user;
	}
}
