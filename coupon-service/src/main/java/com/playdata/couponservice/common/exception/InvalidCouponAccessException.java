package com.playdata.couponservice.common.exception;

public class InvalidCouponAccessException extends Throwable {
    public InvalidCouponAccessException() {
    }

    public InvalidCouponAccessException(String message) {
        super(message);
    }

    public InvalidCouponAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCouponAccessException(Throwable cause) {
        super(cause);
    }

    public InvalidCouponAccessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
