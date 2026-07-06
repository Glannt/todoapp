package com.dotnt.todolist_test.domain.model.todo;

import com.dotnt.todolist_test.domain.model.todoList.TodoList;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class TodoListTest {
    @Test
    void create_withBlankName_shouldThrow() {
        assertThatThrownBy(() -> TodoList.create("", "desc"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_withValidName_shouldSucceed() {
        TodoList list = TodoList.create("Công việc cá nhân", "Mô tả");

        assertThat(list.getName()).isEqualTo("Công việc cá nhân");
        assertThat(list.getId()).isNotNull();
    }

    @Test
    void rename_shouldUpdateNameAndTimestamp() {
        TodoList list = TodoList.create("Old name", null);
        var before = list.getUpdatedAt();

        list.rename("New name");

        assertThat(list.getName()).isEqualTo("New name");
        assertThat(list.getUpdatedAt()).isAfterOrEqualTo(before);
    }
}
