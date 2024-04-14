package com.example.beautybook.exceptions;

public class LoginDeviceLimitExceededException extends RuntimeException {
    public LoginDeviceLimitExceededException(String message) {
        super(message);
    }

    public LoginDeviceLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
