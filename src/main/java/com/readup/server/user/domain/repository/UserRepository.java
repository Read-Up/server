package com.readup.server.user.domain.repository;

import com.readup.server.user.domain.User;

public interface UserRepository {
	User save(User user);

	boolean existsBySocialAccountId(Long socialAccountId);
}
