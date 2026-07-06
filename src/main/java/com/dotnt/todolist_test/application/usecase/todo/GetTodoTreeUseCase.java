package com.dotnt.todolist_test.application.usecase.todo;

import com.dotnt.todolist_test.application.dto.query.TodoView;
import com.dotnt.todolist_test.application.util.SortFieldValidator;
import com.dotnt.todolist_test.domain.common.PageRequest;
import com.dotnt.todolist_test.domain.common.PageResult;
import com.dotnt.todolist_test.domain.exception.TodoListNotFoundException;
import com.dotnt.todolist_test.domain.model.todo.Todo;
import com.dotnt.todolist_test.domain.model.todoList.TodoListId;
import com.dotnt.todolist_test.domain.repository.TodoListRepository;
import com.dotnt.todolist_test.domain.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class GetTodoTreeUseCase {

    private final TodoRepository todoRepository;
    private final TodoListRepository todoListRepository;

    public GetTodoTreeUseCase(TodoRepository todoRepository, TodoListRepository todoListRepository) {
        this.todoRepository = todoRepository;
        this.todoListRepository = todoListRepository;
    }

    public PageResult<TodoView> execute(String listId, int page, int size, String sortBy, String sortDir) {
        TodoListId todoListId = TodoListId.of(listId);
        if (!todoListRepository.existsById(todoListId)) {
            throw new TodoListNotFoundException(listId);
        }

        String validatedField = SortFieldValidator.validate(sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, validatedField, sortDir);

        PageResult<Todo> rootsPage = todoRepository.findRootsByListId(todoListId, pageRequest);

        List<TodoView> views = rootsPage.content().stream()
                .map(this::buildTreeRecursively)
                .toList();
        log.info("Fetched {} root todos for listId: {}, page: {}, size: {}", views.size(), listId, page, size);
        log.debug("Root todos for listId: {}: {}", listId, views);
        for (TodoView view : views
             ) {
            log.info("view content {}", view.content());
            log.info("view children {}", view.children());
            log.info("view listId {}", view.listId());
            log.info("view id {}", view.id());
            log.info("view createdAt {}", view.createdAt());
        }

        return PageResult.of(views, rootsPage.page(), rootsPage.size(), rootsPage.totalElements());
    }

    private TodoView buildTreeRecursively(Todo todo) {
        List<Todo> children = new ArrayList<>(todoRepository.findChildrenOf(todo.getId()));
        children.sort(Comparator.comparingInt(Todo::getSortOrder));

        List<TodoView> childViews = children.stream()
                .map(this::buildTreeRecursively)
                .toList();

        return TodoView.fromWithChildren(todo, childViews);
    }
}
