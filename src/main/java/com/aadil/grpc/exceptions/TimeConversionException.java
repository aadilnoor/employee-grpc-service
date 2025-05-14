package com.aadil.grpc.exceptions;
/**
 * Unchecked exception for time conversion failures
 */
public class TimeConversionException extends RuntimeException {

    public TimeConversionException(String message) {
        super(message);
    }

    public TimeConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}