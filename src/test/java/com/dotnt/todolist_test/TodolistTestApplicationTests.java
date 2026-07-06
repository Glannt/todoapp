package com.dotnt.todolist_test;

import com.dotnt.todolist_test.domain.exception.CircularReferenceException;
import com.dotnt.todolist_test.domain.model.todo.Todo;
import com.dotnt.todolist_test.domain.model.todo.TodoContent;
import com.dotnt.todolist_test.domain.model.todo.TodoId;
import com.dotnt.todolist_test.domain.model.todo.TodoStatus;
import com.dotnt.todolist_test.domain.model.todoList.TodoListId;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
class TodolistTestApplicationTests {

    @Test
    void contextLoads() {
    }


}
