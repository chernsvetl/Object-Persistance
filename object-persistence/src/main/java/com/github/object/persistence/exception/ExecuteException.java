package com.github.object.persistence.exception;

public class ExecuteException extends RuntimeException {
    public ExecuteException(String message, Exception exception) {
        super(message, exception);
    }

    public ExecuteException(Throwable cause) {
        super(cause);
    }

    public ExecuteException(String message) {
        super(message);
    }
}
