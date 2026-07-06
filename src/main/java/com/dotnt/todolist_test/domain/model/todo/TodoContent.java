package com.dotnt.todolist_test.domain.model.todo;

public class TodoContent {
    private final String value;

    private TodoContent(String value) {
        this.value = value;
    }

    public static TodoContent of(String raw) {
        if(raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("Nội dungg công việc không được trống");
        }
        if(raw.length() > 500){
            throw new IllegalArgumentException("Nội dung công việc không được vượt quá 500 ký tự");
        }
        return new TodoContent(raw.trim());
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodoContent that)) return false;
        return value.equals(that.value);
    }
}
