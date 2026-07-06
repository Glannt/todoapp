package com.dotnt.todolist_test.application.dto.command;

public record UpdateTodoListCommand(
        String name,
        String description
) {}
