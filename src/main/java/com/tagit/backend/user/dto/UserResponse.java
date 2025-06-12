package com.tagit.backend.user.dto;

public record UserResponse(
        Long userId,
        String nickname
) {}