package com.tagit.backend.user.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        UserResponse user
) {}