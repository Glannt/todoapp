package com.dotnt.todolist_test.application.usecase.todo;

import com.dotnt.todolist_test.application.dto.command.MoveTodoCommand;
import com.dotnt.todolist_test.application.dto.query.TodoView;
import com.dotnt.todolist_test.domain.exception.TodoNotFoundException;
import com.dotnt.todolist_test.domain.model.todo.Todo;
import com.dotnt.todolist_test.domain.model.todo.TodoId;
import com.dotnt.todolist_test.domain.repository.TodoRepository;
import com.dotnt.todolist_test.domain.service.TodoTreeValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MoveTodoUseCase {

    private final TodoRepository todoRepository;
    private final TodoTreeValidator treeValidator;

    public MoveTodoUseCase(TodoRepository todoRepository, TodoTreeValidator treeValidator) {
        this.todoRepository = todoRepository;
        this.treeValidator = treeValidator;
    }

    @Transactional
    public TodoView execute(MoveTodoCommand command) {
        TodoId todoId = TodoId.of(command.todoId());
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new TodoNotFoundException(command.todoId()));

        TodoId newParentId = null;
        if (command.newParentId() != null && !command.newParentId().isBlank()) {
            newParentId = TodoId.of(command.newParentId());
            if (!todoRepository.existsById(newParentId)) {
                throw new TodoNotFoundException(command.newParentId());
            }
        }

        treeValidator.validateNoCycle(todoId, newParentId);

        todo.changeParent(newParentId);

        Todo saved = todoRepository.save(todo);
        return TodoView.fromFlat(saved);
    }
}
