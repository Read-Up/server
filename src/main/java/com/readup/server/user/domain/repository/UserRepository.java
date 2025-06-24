package com.readup.server.user.domain.repository;

import com.readup.server.user.domain.User;

import java.util.Optional;

public interface UserRepository {
	User save(User user);

	void delete(User user);

	Optional<User> findBySocialAccountId(Long socialAccountId);
}
