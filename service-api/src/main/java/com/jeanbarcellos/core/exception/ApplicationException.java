package com.jeanbarcellos.core.exception;

/**
 * Application Exception
 *
 * @author Jean Silva de Barcellos (jeanbarcellos@hotmail.com)
 */
public class ApplicationException extends RuntimeException {

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

}
