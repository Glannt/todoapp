package com.dotnt.todolist_test.domain.exception;

public class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException(String todoId) {
        super("Không tìm thấy công việc với id: " + todoId);
    }
}
