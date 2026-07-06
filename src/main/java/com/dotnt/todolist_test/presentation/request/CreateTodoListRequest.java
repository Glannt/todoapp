package com.dotnt.todolist_test.presentation.request;

import jakarta.validation.constraints.NotBlank;

public record CreateTodoListRequest(
        @NotBlank(message = "name không được để trống") String name,
        String description
) {}
