package com.dotnt.todolist_test.application.usecase.todolist;

import com.dotnt.todolist_test.application.dto.command.UpdateTodoListCommand;
import com.dotnt.todolist_test.application.dto.query.TodoListView;
import com.dotnt.todolist_test.domain.exception.TodoListNotFoundException;
import com.dotnt.todolist_test.domain.model.todoList.TodoList;
import com.dotnt.todolist_test.domain.model.todoList.TodoListId;
import com.dotnt.todolist_test.domain.repository.TodoListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateTodoListUseCase {

    private final TodoListRepository todoListRepository;

    public UpdateTodoListUseCase(TodoListRepository todoListRepository) {
        this.todoListRepository = todoListRepository;
    }

    @Transactional
    public TodoListView execute(String id, UpdateTodoListCommand command) {
        TodoListId listId = TodoListId.of(id);
        TodoList todoList = todoListRepository.findById(listId)
                .orElseThrow(() -> new TodoListNotFoundException(id));

        todoList.rename(command.name());
        todoList.updateDescription(command.description());

        TodoList saved = todoListRepository.save(todoList);
        return TodoListView.from(saved);
    }
}
