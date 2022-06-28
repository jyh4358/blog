package com.myblog.comment.exception;

public class NotExistCommentException extends RuntimeException{
    private static final String MESSAGE = "댓글이 존재하지 않습니다.";

    public NotExistCommentException() {
        super(MESSAGE);
    }
}
