package com.dotnt.todolist_test.application.util;

import java.util.Set;

public final class SortFieldValidator {

    private static final Set<String> ALLOWED_FIELDS =
            Set.of("content", "status", "sortOrder", "createdAt", "updatedAt");

    private SortFieldValidator() {}

    public static String validate(String field) {
        if (field == null || field.isBlank()) {
            return "sortOrder";
        }
        if (!ALLOWED_FIELDS.contains(field)) {
            throw new IllegalArgumentException(
                    "Trường sắp xếp không hợp lệ: " + field + ". Chỉ chấp nhận: " + ALLOWED_FIELDS);
        }
        return field;
    }
}
