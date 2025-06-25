package com.readup.server.user.application;

import com.readup.server.user.domain.repository.UserRepository;
import com.readup.server.user.dto.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readup.server.user.domain.User;
import com.readup.server.user.dto.UpdateUserRequest;

import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserInfoService {
	private final UserRepository userRepository;

	@Transactional
	public UserResponse updateUser(User user, UpdateUserRequest request) {
		user.update(request.nickname(), request.imageUrl());
		User updatedUser = userRepository.save(user);
		return UserResponse.from(updatedUser);
	}

	@Transactional(readOnly = true)
	public UserResponse getUser(User user) {
		return UserResponse.from(user);
	}
}
