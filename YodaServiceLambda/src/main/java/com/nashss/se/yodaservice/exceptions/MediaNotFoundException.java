package com.nashss.se.yodaservice.exceptions;

public class MediaNotFoundException extends RuntimeException {

    /**
     * Exception to throw when a provided value has invalid attribute values.
     */

    private static final long serialVersionUID = 8007453316698012859L;

    /**
     * Exception with no message or cause.
     */
    public MediaNotFoundException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public MediaNotFoundException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public MediaNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public MediaNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
