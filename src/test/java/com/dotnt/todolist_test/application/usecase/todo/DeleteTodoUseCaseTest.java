package com.dotnt.todolist_test.application.usecase.todo;

import com.dotnt.todolist_test.domain.exception.TodoNotFoundException;
import com.dotnt.todolist_test.domain.model.todo.Todo;
import com.dotnt.todolist_test.domain.model.todo.TodoContent;
import com.dotnt.todolist_test.domain.model.todoList.TodoListId;
import com.dotnt.todolist_test.domain.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteTodoUseCaseTest {

    @Mock private TodoRepository todoRepository;

    @InjectMocks
    private DeleteTodoUseCase useCase;

    @Test
    void execute_shouldDeleteTaskAndAllDescendants() {
        TodoListId listId = TodoListId.generate();
        Todo parent = Todo.createRoot(TodoContent.of("Parent"), listId, 0);
        Todo child = Todo.createChild(TodoContent.of("Child"), listId, parent.getId(), 0);

        when(todoRepository.findById(parent.getId())).thenReturn(Optional.of(parent));
        when(todoRepository.findAllDescendants(parent.getId())).thenReturn(List.of(child));
        when(todoRepository.findById(child.getParentId())).thenReturn(Optional.of(parent));

        useCase.execute(parent.getId().value());

        verify(todoRepository).delete(child);
        verify(todoRepository).delete(parent);
    }

    @Test
    void execute_notFoundTask_shouldThrow() {
        when(todoRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute("non-existent-id"))
                .isInstanceOf(TodoNotFoundException.class);
    }
}
