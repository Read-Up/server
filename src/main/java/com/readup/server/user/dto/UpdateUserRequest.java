package com.readup.server.user.dto;

public record UpdateUserRequest(
        String nickname,
        String imageUrl
) {}