package com.myblog.member.exception;

public class NotExistMemberException extends RuntimeException{
    private static final String MESSAGE = "가입되지 않은 유저입니다. 다시 로그인해 주세요.";

    public NotExistMemberException() {
        super(MESSAGE);
    }
}
