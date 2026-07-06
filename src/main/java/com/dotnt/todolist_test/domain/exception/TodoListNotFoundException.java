package com.dotnt.todolist_test.domain.exception;

public class TodoListNotFoundException extends RuntimeException {
    public TodoListNotFoundException(String todoListId) {
        super("Không tìm thấy danh sách với id: " + todoListId);
    }
}
