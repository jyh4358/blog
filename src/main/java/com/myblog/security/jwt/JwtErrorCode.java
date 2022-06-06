package com.myblog.security.jwt;

import com.myblog.common.ResponseCode;

public enum JwtErrorCode implements ResponseCode {
    TOKEN_NOTFOUND(1300, "인증 토큰이 누락되었습니다."),
    TOKEN_MODULATED(1301, "변조된 토큰입니다."),
    ACCESS_TOKEN_EXPIRATION(1302, "인증 토큰이 만료되었습니다."),
    REFRESH_TOKEN_EXPIRATION(1303, "리프레쉬 토큰이 만료되었습니다."),
    DELETED_REFRESH_TOKEN(1307, "유효하지 않은 리프레시 토큰입니다.");

    private final int code;
    private final String message;

    JwtErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
