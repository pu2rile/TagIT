package com.tagit.backend.user.dto;

public record AuthResponse(
        String token,
        UserResponse user
) {}