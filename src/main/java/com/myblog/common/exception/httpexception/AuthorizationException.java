package com.myblog.common.exception.httpexception;

import com.myblog.common.exception.httpexception.BaseHttpException;

public class AuthorizationException extends BaseHttpException {

    public AuthorizationException(String message, int code) {
        super(message, code);
    }
}
