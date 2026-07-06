package com.dotnt.todolist_test.domain.common;

public record PageRequest(int page, int size, String sortBy, SortDirection direction) {

    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_SIZE = 100;

    public static PageRequest of(int page, int size, String sortBy, String direction) {
        int safePage = Math.max(page, 0);
        int safeSize = size <= 0 ? DEFAULT_SIZE : Math.min(size, MAX_SIZE);
        SortDirection dir = "DESC".equalsIgnoreCase(direction) ? SortDirection.DESC : SortDirection.ASC;
        String field = (sortBy == null || sortBy.isBlank()) ? "sortOrder" : sortBy;
        return new PageRequest(safePage, safeSize, field, dir);
    }
}
