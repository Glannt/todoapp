package com.dotnt.todolist_test.application.usecase.todo;

import com.dotnt.todolist_test.application.dto.command.CreateTodoCommand;
import com.dotnt.todolist_test.domain.common.PageResult;
import com.dotnt.todolist_test.domain.exception.TodoListNotFoundException;
import com.dotnt.todolist_test.domain.exception.TodoNotFoundException;
import com.dotnt.todolist_test.domain.model.todo.TodoId;
import com.dotnt.todolist_test.domain.model.todoList.TodoListId;
import com.dotnt.todolist_test.domain.repository.TodoListRepository;
import com.dotnt.todolist_test.domain.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateTodoUseCaseTest {

    @Mock private TodoRepository todoRepository;
    @Mock private TodoListRepository todoListRepository;

    @InjectMocks
    private CreateTodoUseCase useCase;

    @Test
    void execute_withNonExistentList_shouldThrow() {
        when(todoListRepository.existsById(any())).thenReturn(false);

        CreateTodoCommand command = new CreateTodoCommand("fake-list-id", "content", null);

        assertThatThrownBy(() -> useCase.execute(command))
                .isInstanceOf(TodoListNotFoundException.class);
    }

    @Test
    void execute_asRootTask_shouldSaveWithoutParent() {
        when(todoListRepository.existsById(any())).thenReturn(true);
        when(todoRepository.countRootsByListId(any())).thenReturn(0);   // <-- đổi chỗ này
        when(todoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CreateTodoCommand command = new CreateTodoCommand(
                TodoListId.generate().value(),
                "Task gốc", null);

        var result = useCase.execute(command);

        assertThat(result.parentId()).isNull();
        assertThat(result.content()).isEqualTo("Task gốc");
    }

    @Test
    void execute_asChildTask_shouldSaveWithParent() {
        when(todoListRepository.existsById(any())).thenReturn(true);
        when(todoRepository.existsById(any())).thenReturn(true);
        when(todoRepository.countChildrenOf(any())).thenReturn(0);      // <-- thêm test case cho nhánh con
        when(todoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CreateTodoCommand command = new CreateTodoCommand(
                TodoListId.generate().value(),
                "Subtask",
                TodoId.generate().value());

        var result = useCase.execute(command);

        assertThat(result.parentId()).isNotNull();
        assertThat(result.content()).isEqualTo("Subtask");
    }

    @Test
    void execute_withNonExistentParent_shouldThrow() {
        when(todoListRepository.existsById(any())).thenReturn(true);
        when(todoRepository.existsById(any())).thenReturn(false);

        CreateTodoCommand command = new CreateTodoCommand(
                TodoListId.generate().value(),
                "Subtask", "fake-parent-id");

        assertThatThrownBy(() -> useCase.execute(command))
                .isInstanceOf(TodoNotFoundException.class);
    }
}