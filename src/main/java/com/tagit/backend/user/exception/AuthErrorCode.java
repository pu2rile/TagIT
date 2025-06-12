package com.tagit.backend.user.exception;

import com.tagit.backend.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AuthErrorCode implements ErrorCode {

    ALREADY_EXIST_NICKNAME(HttpStatus.CONFLICT, "AUTH_001", "이미 존재하는 닉네임입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "AUTH_002", "비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH_003", "해당 사용자를 찾을 수 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_004", "유효하지 않은 토큰입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH_005", "토큰이 만료되었습니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    AuthErrorCode(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }
}
