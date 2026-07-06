package com.dotnt.todolist_test.domain.exception;

public class CircularReferenceException extends RuntimeException {
    public CircularReferenceException(String message) {
        super(message);
    }
}
