package com.dotnt.todolist_test.domain.model.todo;

import com.dotnt.todolist_test.domain.exception.CircularReferenceException;
import com.dotnt.todolist_test.domain.model.todoList.TodoListId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Todo {
    private final TodoId id;
    private TodoContent content;
    private TodoStatus status;
    private TodoId parentId;
    private final TodoListId todoListId;
    private int sortOrder;
    private final List<Todo> children = new ArrayList<>();
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Todo(TodoId id, TodoContent content, TodoStatus status, TodoId parentId,
                 TodoListId todoListId, int sortOrder,
                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.content = content;
        this.status = status;
        this.parentId = parentId;
        this.todoListId = todoListId;
        this.sortOrder = sortOrder;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Todo createRoot(TodoContent content, TodoListId todoListId, int sortOrder) {
        LocalDateTime now = LocalDateTime.now();
        return new Todo(TodoId.generate(), content, TodoStatus.PENDING, null,
                todoListId, sortOrder, now, now);
    }

    public static Todo createChild(TodoContent content, TodoListId todoListId,
                                   TodoId parentId, int sortOrder) {
        LocalDateTime now = LocalDateTime.now();
        return new Todo(TodoId.generate(), content, TodoStatus.PENDING, parentId,
                todoListId, sortOrder, now, now);
    }

    public static Todo reconstruct(TodoId id, TodoContent content, TodoStatus status,
                                   TodoId parentId, TodoListId todoListId, int sortOrder,
                                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Todo(id, content, status, parentId, todoListId, sortOrder, createdAt, updatedAt);
    }

    public void rename(TodoContent newContent) {
        this.content = Objects.requireNonNull(newContent);
        touch();
    }

    public void markCompleted() {
        this.status = TodoStatus.COMPLETED;
        touch();
    }

    public void markPending() {
        this.status = TodoStatus.PENDING;
        touch();
    }

    public void toggleStatus() {
        if (this.status == TodoStatus.PENDING) {
            markCompleted();
        } else {
            markPending();
        }
    }

    public void changeParent(TodoId newParentId) {
        if (newParentId != null && newParentId.equals(this.id)) {
            throw new CircularReferenceException("Một task không thể tự làm cha của chính nó");
        }
        this.parentId = newParentId;
        touch();
    }

    public void changeSortOrder(int newSortOrder) {
        this.sortOrder = newSortOrder;
        touch();
    }

    private void touch() {
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isRoot() {
        return parentId == null;
    }

    public boolean isCompleted() {
        return status == TodoStatus.COMPLETED;
    }

    public TodoId getId() {
        return id;
    }

    public TodoContent getContent() {
        return content;
    }

    public TodoStatus getStatus() {
        return status;
    }

    public TodoId getParentId() {
        return parentId;
    }

    public TodoListId getTodoListId() {
        return todoListId;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public List<Todo> getChildren() {
        return children;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Todo)) return false;
        return id.equals(((Todo) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
