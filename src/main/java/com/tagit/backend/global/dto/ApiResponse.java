package com.tagit.backend.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, "요청이 성공적으로 처리되었습니다.");
    }

    public static <T> ApiResponse<T> failure(String errorCode, String message) {
        return new ApiResponse<>(false, null, "[" + errorCode + "] " + message);
    }
}