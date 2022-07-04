package com.myblog.member.exception;

import com.myblog.common.exception.httpexception.BaseHttpException;

public class NotExistMemberException extends BaseHttpException {

    public NotExistMemberException(String message, int code) {
        super(message, code);
    }
}
