package com.dotnt.todolist_test.application.usecase.todo;

import com.dotnt.todolist_test.application.dto.command.UpdateTodoCommand;
import com.dotnt.todolist_test.application.dto.query.TodoView;
import com.dotnt.todolist_test.domain.exception.TodoNotFoundException;
import com.dotnt.todolist_test.domain.model.todo.Todo;
import com.dotnt.todolist_test.domain.model.todo.TodoContent;
import com.dotnt.todolist_test.domain.model.todo.TodoId;
import com.dotnt.todolist_test.domain.repository.TodoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateTodoUseCase {
    private final TodoRepository todoRepository;

    public UpdateTodoUseCase(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Transactional
    public TodoView execute(String id, UpdateTodoCommand command) {
        TodoId todoId = TodoId.of(id);
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new TodoNotFoundException(id));

        todo.rename(TodoContent.of(command.content()));

        Todo saved = todoRepository.save(todo);
        return TodoView.fromFlat(saved);
    }
}
