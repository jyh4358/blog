package com.myblog.category.exception;

public class NotExistCategoryException extends RuntimeException{
    private static final String MESSAGE = "카테고리가 존재하지 않습니다.";

    public NotExistCategoryException() {
        super(MESSAGE);
    }
}
