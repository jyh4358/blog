package com.myblog.common.exception.httpexception;

import com.myblog.common.exception.httpexception.BaseHttpException;

public class S3ImageUploadException extends BaseHttpException {

    public S3ImageUploadException(String message, int code) {
        super(message, code);
    }
}
