package com.myblog.common.exception;

import com.myblog.common.exception.httpexception.AuthorizationException;
import com.myblog.common.exception.httpexception.BaseHttpException;
import com.myblog.common.exception.httpexception.NotExistException;
import com.myblog.common.exception.httpexception.S3ImageUploadException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {
    NOT_FOUND_MEMBER(new NotExistException("해당하는 유저를 찾을 수 없습니다.", 601)),
    NOT_FOUND_CATEGORY(new NotExistException("카테고리가 존재하지 않습니다.", 602)),
    NOT_FOUNT_COMMENT(new NotExistException("댓글이 존재하지 않습니다.", 603)),
    NOT_FOUNT_ARTICLE(new NotExistException("해당 글이 존재하지 않습니다.", 604)),

    INVALID_LOGIN(new AuthorizationException("로그인 후 이용해 주세요.", 701)),
    INVALID_AUTHORIZATION(new AuthorizationException("관리자 계정이 아닙니다.", 702)),

    FAIL_IMAGE_UPLOAD(new S3ImageUploadException("이미지 업로드에 실패했습니다.", 801));

    private final RuntimeException exception;

    public static int getCode(ExceptionMessage exceptionMessage) {
        BaseHttpException exception = (BaseHttpException) exceptionMessage.getException();
        return exception.getCode();
    }
}
