package com.readup.server.user.presentation;

import com.readup.server.common.annotaion.CurrentUser;
import com.readup.server.common.dto.ApiResponse;
import com.readup.server.user.application.RandomNicknameService;
import com.readup.server.user.application.UserInfoService;
import com.readup.server.user.domain.User;
import com.readup.server.user.dto.UpdateUserRequest;
import com.readup.server.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.readup.server.common.dto.ApiResponse.successResponse;


@RestController
@RequiredArgsConstructor
public class UserInfoController {

    private final UserInfoService userInfoService;
    private final RandomNicknameService randomNicknameService;

    @PatchMapping(value = "/private/users", consumes = "multipart/form-data")
    public ApiResponse<UserResponse> updateUser(
            @CurrentUser User user,
            @RequestPart("request") UpdateUserRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return ApiResponse.successResponse(userInfoService.updateUser(user, request, image));
    }

    @GetMapping("/private/users")
    public ApiResponse<UserResponse> getUser(
            @CurrentUser User user
    ) {
        return ApiResponse.successResponse(userInfoService.getUser(user));
    }

    @GetMapping("/public/users/random-nickname")
    public ApiResponse<String> getRandomNickname() {
        return successResponse(randomNicknameService.generate());
    }
}
