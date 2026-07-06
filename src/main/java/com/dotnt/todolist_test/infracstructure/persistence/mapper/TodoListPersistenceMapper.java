package com.dotnt.todolist_test.infracstructure.persistence.mapper;

import com.dotnt.todolist_test.domain.model.todoList.TodoList;
import com.dotnt.todolist_test.domain.model.todoList.TodoListId;
import com.dotnt.todolist_test.infracstructure.persistence.entity.TodoListJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class TodoListPersistenceMapper {

    public TodoListJpaEntity toJpa(TodoList domain) {
        TodoListJpaEntity entity = new TodoListJpaEntity();
        entity.setId(domain.getId().value());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }

    public TodoList toDomain(TodoListJpaEntity entity) {
        return TodoList.reconstruct(
                TodoListId.of(entity.getId()),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
