package com.readup.server.user.infrastructure;

import org.springframework.stereotype.Repository;

import com.readup.server.user.domain.User;
import com.readup.server.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

	private final UserJpaRepository userJpaRepository;

	@Override
	public User save(User user) {
		return userJpaRepository.save(user);
	}

	@Override
	public void delete(User user) {
		userJpaRepository.delete(user);
	}

	@Override
	public Optional<User> findBySocialAccountId(Long socialAccountId) {
		return userJpaRepository.findBySocialAccountId(socialAccountId);
	}
}
