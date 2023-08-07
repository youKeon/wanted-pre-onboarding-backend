package com.yukeon.wantedpreonboardingbackend.member.exception;

import org.springframework.http.HttpStatus;

public class UnAuthorizedMemberException extends RuntimeException {
    private final HttpStatus httpStatus;

    public UnAuthorizedMemberException(final String message, final HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public UnAuthorizedMemberException() {
        this("권한이 없는 사용자입니다.", HttpStatus.FORBIDDEN);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
