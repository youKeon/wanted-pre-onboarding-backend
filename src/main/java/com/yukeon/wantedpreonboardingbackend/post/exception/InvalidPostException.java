package com.yukeon.wantedpreonboardingbackend.post.exception;

public class InvalidPostException extends RuntimeException {
    public InvalidPostException(final String message) {
        super(message);
    }

    public InvalidPostException() {
        this("유효하지 않은 게시글입니다.");
    }

}
