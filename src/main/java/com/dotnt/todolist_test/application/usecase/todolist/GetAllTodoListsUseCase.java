package com.dotnt.todolist_test.application.usecase.todolist;

import com.dotnt.todolist_test.application.dto.query.TodoListView;
import com.dotnt.todolist_test.domain.repository.TodoListRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllTodoListsUseCase {

    private final TodoListRepository todoListRepository;

    public GetAllTodoListsUseCase(TodoListRepository todoListRepository) {
        this.todoListRepository = todoListRepository;
    }

    public List<TodoListView> execute() {
        return todoListRepository.findAll().stream()
                .map(TodoListView::from)
                .toList();
    }
}
