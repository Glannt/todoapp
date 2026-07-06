package com.dotnt.todolist_test.infracstructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "todo", indexes = {
        @Index(name = "idx_todo_list_id", columnList = "list_id"),
        @Index(name = "idx_todo_parent_id", columnList = "parent_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoJpaEntity {

    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "parent_id", length = 36)
    private String parentId;

    @Column(name = "list_id", nullable = false, length = 36)
    private String listId;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
