package com.dotnt.todolist_test.domain.model.todo;

import java.util.Objects;
import java.util.UUID;

public final class TodoId {
    private final String value;

    private TodoId(String value){
        this.value = value;
    }

    public static TodoId generate() {
        return new TodoId(UUID.randomUUID().toString());
    }

    public static TodoId of(String value){
        if(value == null || value.isBlank()) {
            throw new IllegalArgumentException("Id không được để trống");
        }
        return new TodoId(value);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodoId)) return false;
        TodoId that = (TodoId) o;
        return value.equals(that.value);
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
