package com.dotnt.todolist_test.domain.model.todoList;

import java.util.Objects;
import java.util.UUID;

public class TodoListId {
    private final String value;

    private TodoListId(String value) {
        this.value = value;
    }

    public static TodoListId generate() {
        return new TodoListId(UUID.randomUUID().toString());
    }

    public static TodoListId of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("TodoListId không được để trống");
        }
        return new TodoListId(value);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodoListId)) return false;
        return value.equals(((TodoListId) o).value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
