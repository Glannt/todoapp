package com.dotnt.todolist_test.domain.model.todoList;

import java.time.LocalDateTime;
import java.util.Objects;

public class TodoList {
    private static final int NAME_MAX_LENGTH = 200;
    private final TodoListId id;
    private String name;
    private String description;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private TodoList(TodoListId id, String name, String description,
                     LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static TodoList create(String name, String description) {
        validateName(name);
        LocalDateTime now = LocalDateTime.now();
        return new TodoList(TodoListId.generate(), name.trim(), description, now, now);
    }

    public static TodoList reconstruct(TodoListId id, String name, String description,
                                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new TodoList(id, name, description, createdAt, updatedAt);
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Tên danh sách không được để trống");
        }
        if (name.length() > NAME_MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Tên danh sách tối đa " + NAME_MAX_LENGTH + " ký tự");
        }
    }

    public void rename(String newName) {
        validateName(newName);
        this.name = newName.trim();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateDescription(String newDescription) {
        this.description = newDescription;
        this.updatedAt = LocalDateTime.now();
    }

    public TodoListId getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodoList)) return false;
        return id.equals(((TodoList) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
