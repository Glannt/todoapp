package com.dotnt.todolist_test.application.dto.query;

import com.dotnt.todolist_test.domain.model.todo.Todo;

import java.time.LocalDateTime;
import java.util.List;

public record TodoView(
        String id,
        String content,
        String status,
        String parentId,
        String listId,
        int sortOrder,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<TodoView> children
) {
    public static TodoView fromFlat(Todo todo) {
        return new TodoView(
                todo.getId().value(),
                todo.getContent().value(),
                todo.getStatus().name(),
                todo.getParentId() != null ? todo.getParentId().value() : null,
                todo.getTodoListId().value(),
                todo.getSortOrder(),
                todo.getCreatedAt(),
                todo.getUpdatedAt(),
                List.of()
        );
    }

    public static TodoView fromWithChildren(Todo todo, List<TodoView> children) {
        return new TodoView(
                todo.getId().value(),
                todo.getContent().value(),
                todo.getStatus().name(),
                todo.getParentId() != null ? todo.getParentId().value() : null,
                todo.getTodoListId().value(),
                todo.getSortOrder(),
                todo.getCreatedAt(),
                todo.getUpdatedAt(),
                children
        );
    }
}
