package com.readup.server.user.infrastructure;

import org.springframework.stereotype.Repository;

import com.readup.server.user.domain.User;
import com.readup.server.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

	private final UserJpaRepository userJpaRepository;

	@Override
	public User save(User user) {
		return userJpaRepository.save(user);
	}

	@Override
	public boolean existsBySocialAccountId(Long socialAccountId) {
		return userJpaRepository.existsBySocialAccount_Id(socialAccountId);
	}
}
