package com.example.webapp.exeption;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
    }

    public UserNotFoundException(String email) {
        super("User with " + email + " already exists");
    }

    public UserNotFoundException(String email, Throwable cause) {
        super("User with " + email + " already exists", cause);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }

    public UserNotFoundException(String email, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super("User with " + email + " already exists", cause, enableSuppression, writableStackTrace);
    }
}
