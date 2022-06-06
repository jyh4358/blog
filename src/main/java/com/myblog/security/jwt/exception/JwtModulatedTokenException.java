package com.myblog.security.jwt.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtModulatedTokenException extends AuthenticationException {
    public JwtModulatedTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JwtModulatedTokenException(String msg) {
        super(msg);
    }
}
