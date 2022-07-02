package com.myblog.s3.exception;

public class S3ImageUploadException extends RuntimeException{
    private static final String MESSAGE = "이미지 업로드에 실패했습니다.";

    public S3ImageUploadException() {
        super(MESSAGE);
    }
}
