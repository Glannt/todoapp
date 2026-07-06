package com.dotnt.todolist_test.infracstructure.persistence.mapper;

import com.dotnt.todolist_test.domain.model.todo.Todo;
import com.dotnt.todolist_test.domain.model.todo.TodoContent;
import com.dotnt.todolist_test.domain.model.todo.TodoId;
import com.dotnt.todolist_test.domain.model.todo.TodoStatus;
import com.dotnt.todolist_test.domain.model.todoList.TodoListId;
import com.dotnt.todolist_test.infracstructure.persistence.entity.TodoJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class TodoPersistenceMapper {

    public TodoJpaEntity toJpa(Todo domain) {
        TodoJpaEntity entity = new TodoJpaEntity();
        entity.setId(domain.getId().value());
        entity.setContent(domain.getContent().value());
        entity.setStatus(domain.getStatus().name());
        entity.setParentId(domain.getParentId() != null ? domain.getParentId().value() : null);
        entity.setListId(domain.getTodoListId().value());
        entity.setSortOrder(domain.getSortOrder());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }

    public Todo toDomain(TodoJpaEntity entity) {
        return Todo.reconstruct(
                TodoId.of(entity.getId()),
                TodoContent.of(entity.getContent()),
                TodoStatus.valueOf(entity.getStatus()),
                entity.getParentId() != null ? TodoId.of(entity.getParentId()) : null,
                TodoListId.of(entity.getListId()),
                entity.getSortOrder(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}