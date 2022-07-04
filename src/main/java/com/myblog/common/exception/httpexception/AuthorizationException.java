package com.myblog.common.exception.httpexception;

public class AuthorizationException extends BaseHttpException {

    public AuthorizationException(String message, int code) {
        super(message, code);
    }
}
