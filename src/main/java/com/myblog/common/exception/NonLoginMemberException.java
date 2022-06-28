package com.myblog.common.exception;

public class NonLoginMemberException extends RuntimeException {
    private static final String MESSAGE = "로그인 후 이용해 주세요.";

    public NonLoginMemberException() {
        super(MESSAGE);
    }
}
