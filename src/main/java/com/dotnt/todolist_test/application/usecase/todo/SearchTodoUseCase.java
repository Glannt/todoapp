package com.dotnt.todolist_test.application.usecase.todo;

import com.dotnt.todolist_test.application.dto.query.TodoView;
import com.dotnt.todolist_test.application.util.SortFieldValidator;
import com.dotnt.todolist_test.domain.common.PageRequest;
import com.dotnt.todolist_test.domain.common.PageResult;
import com.dotnt.todolist_test.domain.exception.TodoListNotFoundException;
import com.dotnt.todolist_test.domain.model.todo.Todo;
import com.dotnt.todolist_test.domain.model.todo.TodoStatus;
import com.dotnt.todolist_test.domain.model.todoList.TodoListId;
import com.dotnt.todolist_test.domain.repository.TodoListRepository;
import com.dotnt.todolist_test.domain.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchTodoUseCase {

    private final TodoRepository todoRepository;
    private final TodoListRepository todoListRepository;

    public SearchTodoUseCase(TodoRepository todoRepository, TodoListRepository todoListRepository) {
        this.todoRepository = todoRepository;
        this.todoListRepository = todoListRepository;
    }

    public PageResult<TodoView> execute(String listId, String keyword, String status,
                                        int page, int size, String sortBy, String sortDir) {
        TodoListId todoListId = TodoListId.of(listId);
        if (!todoListRepository.existsById(todoListId)) {
            throw new TodoListNotFoundException(listId);
        }

        TodoStatus todoStatus = (status == null || status.isBlank())
                ? null
                : TodoStatus.valueOf(status.toUpperCase());

        String validatedField = SortFieldValidator.validate(sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, validatedField, sortDir);

        PageResult<Todo> resultsPage = todoRepository.search(todoListId, keyword, todoStatus, pageRequest);

        java.util.List<TodoView> views = resultsPage.content().stream()
                .map(TodoView::fromFlat)
                .toList();

        return PageResult.of(views, resultsPage.page(), resultsPage.size(), resultsPage.totalElements());
    }
}
