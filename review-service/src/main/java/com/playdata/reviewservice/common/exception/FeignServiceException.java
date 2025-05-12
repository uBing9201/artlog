package com.playdata.reviewservice.common.exception;

public class FeignServiceException extends Throwable {
    public FeignServiceException() {
    }

    public FeignServiceException(String message) {
        super(message);
    }

    public FeignServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public FeignServiceException(Throwable cause) {
        super(cause);
    }

    public FeignServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
