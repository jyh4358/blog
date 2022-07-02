package com.myblog.common.exception.exceptionadvice;

import com.myblog.article.exception.NotExistArticleException;
import com.myblog.comment.exception.NotExistCommentException;
import com.myblog.common.exception.ExceptionMessage;
import com.myblog.common.exception.NonAuthenticationException;
import com.myblog.common.exception.NonLoginMemberException;
import com.myblog.member.exception.NotExistMemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice(basePackages = "com.myblog")
public class ExceptionAdviceController {

    // 잘못된 입력
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionMessage> handleException(Exception e) {
        log.error(e.getClass() + ": " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        return new ResponseEntity<ExceptionMessage>(ExceptionMessage.of(Arrays.toString(e.getStackTrace())), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotExistMemberException.class)
    public ResponseEntity<ExceptionMessage> handleNotExistMemberException(Exception e) {
        log.error(e.getClass() + ": " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        return new ResponseEntity<>(ExceptionMessage.of(e.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotExistCommentException.class)
    public ResponseEntity<ExceptionMessage> handleNotExistCommentException(Exception e) {
        log.error(e.getClass() + ": " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        return new ResponseEntity<>(ExceptionMessage.of(e.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotExistArticleException.class)
    public ResponseEntity<ExceptionMessage> handleNotExistArticleException(Exception e) {
        log.error(e.getClass() + ": " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        return new ResponseEntity<>(ExceptionMessage.of(e.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NonLoginMemberException.class)
    public ResponseEntity<ExceptionMessage> handleNonLoginMemberException(Exception e) {
        log.error(e.getClass() + ": " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        return new ResponseEntity<>(ExceptionMessage.of(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NonAuthenticationException.class)
    public ResponseEntity<ExceptionMessage> NonAuthenticationException(Exception e) {
        log.error(e.getClass() + ": " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        return new ResponseEntity<>(ExceptionMessage.of(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
