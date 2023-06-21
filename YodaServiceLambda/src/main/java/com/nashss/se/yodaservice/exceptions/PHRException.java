package com.nashss.se.yodaservice.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;

public class PHRException extends Throwable {
    /**
     * Exception to throw when a provided value has invalid attribute values.
     */

    private static final long serialVersionUID = 8007453317798002839L;

    /**
     * Exception with no message or cause.
     */
    public PHRException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public PHRException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public PHRException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public PHRException(String message, Throwable cause) {
        super(message, cause);
    }
}
