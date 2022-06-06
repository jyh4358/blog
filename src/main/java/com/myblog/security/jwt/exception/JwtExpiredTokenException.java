package com.myblog.security.jwt.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtExpiredTokenException extends AuthenticationException {

    public JwtExpiredTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JwtExpiredTokenException(String msg) {
        super(msg);
    }
}
