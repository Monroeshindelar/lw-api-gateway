package com.mshindelar.lwapigateway.exception;

public class MissingAuthCredentialsException extends RuntimeException {
    public MissingAuthCredentialsException() { super() ; }

    public MissingAuthCredentialsException(String message) { super(message); }
}
