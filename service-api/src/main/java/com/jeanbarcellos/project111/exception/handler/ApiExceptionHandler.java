package com.jeanbarcellos.project111.exception.handler;


import java.util.Arrays;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.jeanbarcellos.core.constants.MessageConstants;
import com.jeanbarcellos.core.dto.ErrorResponse;
import com.jeanbarcellos.core.exception.ApplicationException;
import com.jeanbarcellos.core.exception.DomainException;
import com.jeanbarcellos.core.exception.NotFoundException;
import com.jeanbarcellos.core.exception.ValidationException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    private static final String MSG_ERROR_HTTP_METHOD_NOT_SUPPORTED = "HTTP method not supported: %s";

    // App ----------------

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handle(DomainException exception) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .body(ErrorResponse.of(exception.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(NotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND.value())
                .body(ErrorResponse.of(exception.getMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handle(ValidationException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST.value())
                .body(ErrorResponse.of(exception.getMessage(), exception.getErrors()));
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handle(ApplicationException exception) {
        log.error(exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(ErrorResponse.of(exception.getMessage()));
    }

    // Hibernate ----------------

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handle(OptimisticLockingFailureException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT.value())
                .body(ErrorResponse.of(MessageConstants.MSG_ERROR_OPTIMISTIC_LOCKING));
    }

    // Spring Web ----------------

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handle(NoResourceFoundException exception) {
        log.error(exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND.value())
                .body(ErrorResponse.of(MessageConstants.MSG_ERROR_NOT_FOUND));
    }

    @SuppressWarnings("null")
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handle(MethodArgumentTypeMismatchException exception) {
        log.error(exception.getMessage());

        var type = ObjectUtils.isEmpty(exception.getRequiredType()) ? String.class : exception.getRequiredType();
        String pattern = String.format("should be of type %s", type.getSimpleName());

        String error = String.format(MessageConstants.MSG_ERROR_VALIDATION_FIELD_LIST, exception.getName(), pattern);

        var response = ErrorResponse.of(MessageConstants.MSG_ERROR_VALIDATION, Arrays.asList(error));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST.value())
                .body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handle(HttpRequestMethodNotSupportedException exception) {
        log.error(exception.getMessage());

        String error = String.format(MSG_ERROR_HTTP_METHOD_NOT_SUPPORTED, exception.getMethod());

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ErrorResponse.of(error));
    }

    // Todo resto ---------------------------------------------------

    // TODO Criar manipuladores e tratamentos para informações erradas como JSON, parametros, etc
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handle(Throwable exception) {
        log.error(exception.getMessage(), exception);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(ErrorResponse.of(MessageConstants.MSG_ERROR_SERVICE));
    }

}
