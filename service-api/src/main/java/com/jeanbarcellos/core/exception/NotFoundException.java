package com.jeanbarcellos.core.exception;

/**
 * @author Jean Silva de Barcellos (jeanbarcellos@hotmail.com)
 */
public class NotFoundException extends ApplicationException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }


}
