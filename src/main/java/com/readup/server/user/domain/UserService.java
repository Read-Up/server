package com.readup.server.user.domain;

import org.springframework.stereotype.Service;

import com.readup.server.common.exception.DomainException;
import com.readup.server.common.exception.ErrorCode;
import com.readup.server.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public User save(User user) {
		return userRepository.save(user);
	}

	public void existsBySocialAccountId(Long socialAccountId) {
		if (userRepository.existsBySocialAccountId(socialAccountId)) {
			throw new DomainException(ErrorCode.ALREADY_REGISTERED_USER);
		}
	}
}
