package com.tagit.backend.tag.exception;

import com.tagit.backend.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum NoteErrorCode implements ErrorCode {
    NOTE_NOT_FOUND(HttpStatus.NOT_FOUND, "NOTE_001", "해당 메모를 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "NOTE_002", "유저를 찾을 수 없습니다."),
    TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "NOTE_003", "태그를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    NoteErrorCode(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() { return httpStatus; }

    @Override
    public String getMessage() { return message; }

    @Override
    public String getErrorCode() { return errorCode; }
}
