package com.dotnt.todolist_test.application.usecase.todolist;

import com.dotnt.todolist_test.application.dto.command.CreateTodoListCommand;
import com.dotnt.todolist_test.application.dto.query.TodoListView;
import com.dotnt.todolist_test.domain.model.todoList.TodoList;
import com.dotnt.todolist_test.domain.repository.TodoListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateTodoListUseCase {

    private final TodoListRepository todoListRepository;

    public CreateTodoListUseCase(TodoListRepository todoListRepository) {
        this.todoListRepository = todoListRepository;
    }

    @Transactional
    public TodoListView execute(CreateTodoListCommand command) {
        TodoList todoList = TodoList
                .create(command.name(), command.description());
        TodoList saved = todoListRepository.save(todoList);
        return TodoListView.from(saved);
    }
}
