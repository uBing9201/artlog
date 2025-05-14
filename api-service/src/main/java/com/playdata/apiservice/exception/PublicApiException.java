package com.playdata.apiservice.exception;

public class PublicApiException extends RuntimeException {
    public PublicApiException() {
    }

    public PublicApiException(String message) {
        super(message);
    }

    public PublicApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public PublicApiException(Throwable cause) {
        super(cause);
    }

    public PublicApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
