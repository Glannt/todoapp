package com.dotnt.todolist_test.application.dto.query;

import com.dotnt.todolist_test.domain.model.todoList.TodoList;

import java.time.LocalDateTime;

public record TodoListView(
        String id,
        String name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static TodoListView from(TodoList todoList) {
        return new TodoListView(
                todoList.getId().value(),
                todoList.getName(),
                todoList.getDescription(),
                todoList.getCreatedAt(),
                todoList.getUpdatedAt()
        );
    }
}
