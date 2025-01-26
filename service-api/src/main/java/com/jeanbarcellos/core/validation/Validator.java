package com.jeanbarcellos.core.validation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.jeanbarcellos.core.constants.MessageConstants;
import com.jeanbarcellos.core.exception.ValidationException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;

/**
 * Validator Service
 *
 * @author Jean Silva de Barcellos (jeanbarcellos@hotmail.com)
 *
 * @see https://github.com/jeanbarcellos/project-106/blob/master/src/main/java/com/jeanbarcellos/core/validation/Validator.java
 */
public class Validator {

    public <T> Set<ConstraintViolation<T>> check(T model) {
        return this.getInnerValidator().validate(model);
    }

    public <T> void validate(T model) {
        var constraintViolations = this.check(model);

        if (!constraintViolations.isEmpty()) {
            throw createValidateException(constraintViolations);
        }
    }

    public static <T> ValidationException createValidateException(Set<ConstraintViolation<T>> constraintViolations) {
        return ValidationException.of(MessageConstants.MSG_ERROR_VALIDATION, createMessages(constraintViolations));
    }

    public static <T> List<String> createMessages(Set<ConstraintViolation<T>> constraintViolations) {
        return constraintViolations.stream()
                .map(Validator::getMessage)
                .collect(Collectors.toList());
    }

    private static <T> String getMessage(ConstraintViolation<T> constraintViolation) {
        return String.format(MessageConstants.MSG_ERROR_VALIDATION_FIELD_LIST,
                constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
    }

    private jakarta.validation.Validator getInnerValidator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }
}
