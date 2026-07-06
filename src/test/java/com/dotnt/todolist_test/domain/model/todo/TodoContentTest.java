package com.dotnt.todolist_test.domain.model.todo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class TodoContentTest {
    @Test
    void of_withBlankValue_shouldThrow() {
        assertThatThrownBy(() -> TodoContent.of("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("không được trống");
    }

    @Test
    void of_exceedingMaxLength_shouldThrow() {
        String tooLong = "a".repeat(501);

        assertThatThrownBy(() -> TodoContent.of(tooLong))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("vượt quá");
    }

    @Test
    void of_withValidValue_shouldTrimWhitespace() {
        TodoContent content = TodoContent.of("  Học Java  ");

        assertThat(content.value()).isEqualTo("Học Java");
    }
}
