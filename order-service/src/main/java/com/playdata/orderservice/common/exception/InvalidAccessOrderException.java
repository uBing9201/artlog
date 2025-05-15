package com.playdata.orderservice.common.exception;

public class InvalidAccessOrderException extends Throwable {
    public InvalidAccessOrderException() {
    }

    public InvalidAccessOrderException(String message) {
        super(message);
    }

    public InvalidAccessOrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAccessOrderException(Throwable cause) {
        super(cause);
    }

    public InvalidAccessOrderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
