package com.dotnt.todolist_test.application.dto.command;

public record CreateTodoCommand(
        String listId,
        String content,
        String parentId
) {}
