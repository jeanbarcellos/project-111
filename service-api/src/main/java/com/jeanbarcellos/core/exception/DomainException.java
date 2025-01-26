package com.jeanbarcellos.core.exception;

/**
 * @author Jean Silva de Barcellos (jeanbarcellos@hotmail.com)
 */
public class DomainException extends ApplicationException {

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }

}
