package com.yukeon.wantedpreonboardingbackend.member.exception;

public class InvalidMemberException extends RuntimeException {
    public InvalidMemberException(final String message) {
        super(message);
    }

    public InvalidMemberException() {
        this("유효하지 않은 사용자입니다.");
    }

}
