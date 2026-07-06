package com.dotnt.todolist_test.domain.repository;

import com.dotnt.todolist_test.domain.model.todoList.TodoList;
import com.dotnt.todolist_test.domain.model.todoList.TodoListId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface TodoListRepository {
    TodoList save(TodoList todoList);

    Optional<TodoList> findById(TodoListId id);

    List<TodoList> findAll();

    void delete(TodoList todoList);

    boolean existsById(TodoListId id);
}
