package com.myblog.common.exception;

public class NonAuthenticationException extends RuntimeException {
    private static final String MESSAGE = "관리자 계정이 아닙니다.";

    public NonAuthenticationException() {
        super(MESSAGE);
    }
}
