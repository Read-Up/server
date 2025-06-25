package com.readup.server.user.application;

import com.readup.server.common.util.R2Uploader;
import com.readup.server.user.domain.repository.UserRepository;
import com.readup.server.user.dto.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readup.server.user.domain.User;
import com.readup.server.user.dto.UpdateUserRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserInfoService {
	private final UserRepository userRepository;
	private final R2Uploader r2Uploader;

	@Transactional
	public UserResponse updateUser(User user, UpdateUserRequest request, MultipartFile image) {
		String imageUrl = user.getImageUrl();
		if (image != null && !image.isEmpty()) {
			imageUrl = r2Uploader.uploadSingle(image, user.getId(), "profile");
		}
		user.update(request.nickname(), imageUrl);
		return UserResponse.from(user);
	}

	@Transactional(readOnly = true)
	public UserResponse getUser(User user) {
		return UserResponse.from(user);
	}
}
