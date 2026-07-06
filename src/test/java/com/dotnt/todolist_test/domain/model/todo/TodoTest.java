package com.dotnt.todolist_test.domain.model.todo;

import com.dotnt.todolist_test.domain.exception.CircularReferenceException;
import com.dotnt.todolist_test.domain.model.todoList.TodoListId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class TodoTest {

    private final TodoListId listId = TodoListId.generate();

    @Test
    void createRoot_shouldHavePendingStatusAndNoParent() {
        Todo todo = Todo.createRoot(TodoContent.of("Học Spring Boot"), listId, 0);

        assertThat(todo.isRoot()).isTrue();
        assertThat(todo.getStatus()).isEqualTo(TodoStatus.PENDING);
        assertThat(todo.isCompleted()).isFalse();
    }

    @Test
    void createChild_shouldHaveParentId() {
        TodoId parentId = TodoId.generate();
        Todo child = Todo.createChild(TodoContent.of("Subtask"), listId, parentId, 0);

        assertThat(child.isRoot()).isFalse();
        assertThat(child.getParentId()).isEqualTo(parentId);
    }

    @Test
    void toggleStatus_shouldSwitchBetweenPendingAndCompleted() {
        Todo todo = Todo.createRoot(TodoContent.of("Task"), listId, 0);

        todo.toggleStatus();
        assertThat(todo.getStatus()).isEqualTo(TodoStatus.COMPLETED);

        todo.toggleStatus();
        assertThat(todo.getStatus()).isEqualTo(TodoStatus.PENDING);
    }

    @Test
    void rename_shouldUpdateContent() {
        Todo todo = Todo.createRoot(TodoContent.of("Old content"), listId, 0);

        todo.rename(TodoContent.of("New content"));

        assertThat(todo.getContent().value()).isEqualTo("New content");
    }

    @Test
    void changeParent_toItself_shouldThrowCircularReferenceException() {
        Todo todo = Todo.createRoot(TodoContent.of("Task"), listId, 0);

        assertThatThrownBy(() -> todo.changeParent(todo.getId()))
                .isInstanceOf(CircularReferenceException.class);
    }

    @Test
    void changeParent_toValidId_shouldSucceed() {
        Todo todo = Todo.createRoot(TodoContent.of("Task"), listId, 0);
        TodoId newParent = TodoId.generate();

        todo.changeParent(newParent);

        assertThat(todo.getParentId()).isEqualTo(newParent);
        assertThat(todo.isRoot()).isFalse();
    }

}
