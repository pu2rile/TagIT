package com.tagit.backend.global.dto;

public record ApiResponse<T>(
        boolean success,
        String message,
        T content
) {
    public static <T> ApiResponse<T> success(T content) {
        return new ApiResponse<>(true, "요청이 성공적으로 처리되었습니다.", content);
    }

    public static <T> ApiResponse<T> success(String message, T content) {
        return new ApiResponse<>(true, message, content);
    }

    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(false, message, null);
    }

    public static <T> ApiResponse<T> failure(String errorCode, String message) {
        return new ApiResponse<>(false, "[" + errorCode + "] " + message, null);
    }
}