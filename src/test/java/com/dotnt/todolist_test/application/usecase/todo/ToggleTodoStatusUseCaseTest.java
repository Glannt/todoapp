package com.dotnt.todolist_test.application.usecase.todo;

import com.dotnt.todolist_test.domain.exception.TodoNotFoundException;
import com.dotnt.todolist_test.domain.model.todo.Todo;
import com.dotnt.todolist_test.domain.model.todo.TodoContent;
import com.dotnt.todolist_test.domain.model.todo.TodoStatus;
import com.dotnt.todolist_test.domain.model.todoList.TodoListId;
import com.dotnt.todolist_test.domain.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ToggleTodoStatusUseCaseTest {

    @Mock private TodoRepository todoRepository;

    @InjectMocks
    private ToggleTodoStatusUseCase useCase;

    @Test
    void execute_pendingTask_shouldBecomeCompleted() {
        Todo todo = Todo.createRoot(TodoContent.of("Task"), TodoListId.generate(), 0);
        when(todoRepository.findById(todo.getId())).thenReturn(Optional.of(todo));
        when(todoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = useCase.execute(todo.getId().value());

        assertThat(result.status()).isEqualTo(TodoStatus.COMPLETED.name());
    }

    @Test
    void execute_notFoundTask_shouldThrow() {
        when(todoRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute("non-existent-id"))
                .isInstanceOf(TodoNotFoundException.class);
    }
}
