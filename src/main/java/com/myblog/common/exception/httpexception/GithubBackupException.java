package com.myblog.common.exception.httpexception;

public class GithubBackupException extends BaseHttpException {

    public GithubBackupException(String message, int code) {
        super(message, code);
    }
}
