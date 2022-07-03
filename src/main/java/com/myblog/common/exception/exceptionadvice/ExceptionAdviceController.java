package com.myblog.common.exception.exceptionadvice;

import com.myblog.common.exception.httpexception.AuthorizationException;
import com.myblog.common.exception.ErrorResponse;
import com.myblog.common.exception.httpexception.GithubBackupException;
import com.myblog.common.exception.httpexception.NotExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ExceptionAdviceController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.warn("MethodArgumentNotValidException {}", exception.getMessage(), exception);
        BindingResult result = exception.getBindingResult();
        return ErrorResponse.error(HttpStatus.BAD_REQUEST.value(), result.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList()));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ErrorResponse handleException(Exception exception) {
        log.error("Exception/RuntimeException {}", exception.getMessage(), exception);
        return ErrorResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotExistException.class)
    public ErrorResponse handleNotExistException(NotExistException e) {
        log.error(e.getClass() + ": " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        return ErrorResponse.error(e.getCode(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthorizationException.class)
    public ErrorResponse handleAuthenticationException(AuthorizationException e) {
        log.error(e.getClass() + ": " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        return ErrorResponse.error(e.getCode(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(GithubBackupException.class)
    public ErrorResponse handleGithubBackupException(GithubBackupException e) {
        log.error(e.getClass() + ": " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        return ErrorResponse.error(e.getCode(), e.getMessage());
    }


}
