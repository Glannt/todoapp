package com.dotnt.todolist_test.presentation.request;

import jakarta.validation.constraints.NotBlank;

public record CreateTodoRequest(
        @NotBlank(message = "listId không được để trống") String listId,
        @NotBlank(message = "content không được để trống") String content,
        String parentId
) {}
