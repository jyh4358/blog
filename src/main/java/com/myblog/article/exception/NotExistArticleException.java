package com.myblog.article.exception;

public class NotExistArticleException extends RuntimeException{
    private static final String MESSAGE = "해당 글이 존재하지 않습니다.";
    public NotExistArticleException() {
        super(MESSAGE);
    }
}
