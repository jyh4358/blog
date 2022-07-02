package com.myblog.common.exception.httpexception;

public class NotExistException extends BaseHttpException {

    public NotExistException(String message, int code) {
        super(message, code);
    }
}
