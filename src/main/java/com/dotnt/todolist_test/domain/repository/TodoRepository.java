package com.dotnt.todolist_test.domain.repository;

import com.dotnt.todolist_test.domain.common.PageRequest;
import com.dotnt.todolist_test.domain.common.PageResult;
import com.dotnt.todolist_test.domain.model.todo.Todo;
import com.dotnt.todolist_test.domain.model.todo.TodoId;
import com.dotnt.todolist_test.domain.model.todo.TodoStatus;
import com.dotnt.todolist_test.domain.model.todoList.TodoListId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface TodoRepository {
    Todo save(Todo todo);

    Optional<Todo> findById(TodoId id);

    PageResult<Todo> findRootsByListId(TodoListId listId, PageRequest pageRequest);

    List<Todo> findChildrenOf(TodoId parentId);

    List<Todo> findAllDescendants(TodoId id);

    PageResult<Todo> search(TodoListId listId, String keyword, TodoStatus status, PageRequest pageRequest);

    List<Todo> findAllRootsByListId(TodoListId listId);

    void delete(Todo todo);

    boolean existsById(TodoId id);

    int countRootsByListId(TodoListId listId);

    int countChildrenOf(TodoId parentId);
}
