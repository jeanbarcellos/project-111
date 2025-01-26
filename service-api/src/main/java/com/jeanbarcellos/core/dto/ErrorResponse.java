package com.jeanbarcellos.core.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * Response to Error with list of details
 *
 * @author Jean Silva de Barcellos (jeanbarcellos@hotmail.com)
 */
@Getter
public class ErrorResponse {

    @Schema(name = "status", description = "Status Code")
    private Integer status;

    @Schema(name = "message", description = "Mensagem")
    private String message;

    @Schema(description = "Detalhes do erro")
    @JsonInclude(Include.NON_NULL)
    private List<String> errors;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public ErrorResponse(String message, List<String> errors) {
        this.message = message;
        this.errors = errors;
    }

    public boolean hasErros() {
        return !this.errors.isEmpty();
    }

    public static ErrorResponse of(String message) {
        return new ErrorResponse(message);
    }

    public static ErrorResponse of(String message, List<String> errors) {
        return new ErrorResponse(message, errors);
    }

}
