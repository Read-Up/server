package com.readup.server.user.application;

import org.springframework.stereotype.Service;

import com.readup.server.user.domain.User;
import com.readup.server.user.infrastructure.UserJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserJpaRepository userJpaRepository;

	public User save(String nickname) {
		return userJpaRepository.save(User.builder().nickname(nickname).build());
	}
}
