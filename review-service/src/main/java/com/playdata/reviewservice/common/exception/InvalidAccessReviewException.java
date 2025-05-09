package com.playdata.reviewservice.common.exception;

public class InvalidAccessReviewException extends Throwable {
    public InvalidAccessReviewException() {
    }

    public InvalidAccessReviewException(String message) {
        super(message);
    }

    public InvalidAccessReviewException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAccessReviewException(Throwable cause) {
        super(cause);
    }

    public InvalidAccessReviewException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
