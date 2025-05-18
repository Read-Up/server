package com.readup.server.user.domain;

import org.springframework.stereotype.Service;

import com.readup.server.common.exception.DomainException;
import com.readup.server.common.exception.ErrorCode;
import com.readup.server.user.infrastructure.UserJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserJpaRepository userJpaRepository;

	public User save(User user) {
		return userJpaRepository.save(user);
	}

	public void existsBySocialAccountId(Long socialAccountId) {
		if (userJpaRepository.existsBySocialAccount_Id(socialAccountId)) {
			throw new DomainException(ErrorCode.ALREADY_REGISTERED_USER);
		}
	}
}
