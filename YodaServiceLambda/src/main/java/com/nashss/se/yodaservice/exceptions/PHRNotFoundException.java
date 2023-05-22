package com.nashss.se.yodaservice.exceptions;

public class PHRNotFoundException extends RuntimeException {
    /**
     * Exception to throw when a provided value has invalid attribute values.
     */

    private static final long serialVersionUID = 8007453316698002639L;

    /**
     * Exception with no message or cause.
     */
    public PHRNotFoundException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public PHRNotFoundException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public PHRNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public PHRNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
