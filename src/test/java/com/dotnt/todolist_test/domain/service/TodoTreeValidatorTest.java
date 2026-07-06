package com.dotnt.todolist_test.domain.service;

import com.dotnt.todolist_test.domain.exception.CircularReferenceException;
import com.dotnt.todolist_test.domain.model.todo.Todo;
import com.dotnt.todolist_test.domain.model.todo.TodoContent;
import com.dotnt.todolist_test.domain.model.todo.TodoId;
import com.dotnt.todolist_test.domain.model.todo.TodoStatus;
import com.dotnt.todolist_test.domain.model.todoList.TodoListId;
import com.dotnt.todolist_test.domain.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoTreeValidatorTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoTreeValidator validator;

    @Test
    void validateNoCycle_withNullNewParent_shouldNotThrow() {
        TodoId todoId = TodoId.generate();

        assertThatCode(() -> validator.validateNoCycle(todoId, null))
                .doesNotThrowAnyException();
    }

    @Test
    void validateNoCycle_movingIntoItself_shouldThrow() {
        TodoId todoId = TodoId.generate();

        assertThatThrownBy(() -> validator.validateNoCycle(todoId, todoId))
                .isInstanceOf(CircularReferenceException.class);
    }

    @Test
    void validateNoCycle_movingIntoOwnDescendant_shouldThrow() {
        TodoId todoId = TodoId.generate();
        TodoId descendantId = TodoId.generate();
        TodoListId listId = TodoListId.generate();

        Todo descendant = Todo.createChild(TodoContent.of("child"), listId, todoId, 0);
        // giả lập descendant có id = descendantId dùng reconstruct
        Todo fakeDescendant = Todo.reconstruct(descendantId, TodoContent.of("child"),
                TodoStatus.PENDING, todoId, listId, 0,
                java.time.LocalDateTime.now(), java.time.LocalDateTime.now());

        when(todoRepository.findAllDescendants(todoId)).thenReturn(List.of(fakeDescendant));

        assertThatThrownBy(() -> validator.validateNoCycle(todoId, descendantId))
                .isInstanceOf(CircularReferenceException.class);
    }

    @Test
    void validateNoCycle_movingToUnrelatedNode_shouldNotThrow() {
        TodoId todoId = TodoId.generate();
        TodoId unrelatedId = TodoId.generate();

        when(todoRepository.findAllDescendants(todoId)).thenReturn(List.of());

        assertThatCode(() -> validator.validateNoCycle(todoId, unrelatedId))
                .doesNotThrowAnyException();
    }
}
