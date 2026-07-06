package com.dotnt.todolist_test.presentation.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateTodoRequest(
        @NotBlank(message = "content không được để trống") String content
) {}
