package com.jeanbarcellos.core.exception;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Jean Silva de Barcellos (jeanbarcellos@hotmail.com)
 */
public class ValidationException extends ApplicationException {

    public static final String ERRORS_PREFIX = "Errors=";

    private final List<String> errors;

    public ValidationException(String message) {
        super(message);
        this.errors = null;
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        this.errors = null;
    }

    public ValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public ValidationException(String message, String... errors) {
        super(message);
        this.errors = Arrays.asList(errors);
    }

    public ValidationException(String message, List<String> errors, Throwable cause) {
        super(message, cause);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return this.errors;
    }

    public boolean hasErrors() {
        return ObjectUtils.isNotEmpty(this.errors);
    }

    public String getMessageToLog() {
        var message = this.getMessage();

        if (this.hasErrors()) {
            message += StringUtils.LF + ERRORS_PREFIX + this.getErrors().toString();
        }

        return message;
    }

    public static ValidationException of(String message) {
        return new ValidationException(message);
    }

    public static ValidationException of(String message, List<String> errors) {
        return new ValidationException(message, errors);
    }

}