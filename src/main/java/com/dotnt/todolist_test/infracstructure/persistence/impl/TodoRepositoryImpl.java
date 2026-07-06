package com.dotnt.todolist_test.infracstructure.persistence.impl;

import com.dotnt.todolist_test.domain.common.PageResult;
import com.dotnt.todolist_test.domain.model.todo.Todo;
import com.dotnt.todolist_test.domain.model.todo.TodoId;
import com.dotnt.todolist_test.domain.model.todo.TodoStatus;
import com.dotnt.todolist_test.domain.model.todoList.TodoListId;
import com.dotnt.todolist_test.domain.repository.TodoRepository;
import com.dotnt.todolist_test.infracstructure.persistence.entity.TodoJpaEntity;
import com.dotnt.todolist_test.infracstructure.persistence.mapper.TodoPersistenceMapper;
import com.dotnt.todolist_test.infracstructure.persistence.repository.TodoJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TodoRepositoryImpl implements TodoRepository {

    private final TodoJpaRepository jpaRepository;
    private final TodoPersistenceMapper mapper;

    public TodoRepositoryImpl(TodoJpaRepository jpaRepository, TodoPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Todo save(Todo todo) {
        TodoJpaEntity saved = jpaRepository.save(mapper.toJpa(todo));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Todo> findById(TodoId id) {
        return jpaRepository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public PageResult<Todo> findRootsByListId(TodoListId listId, com.dotnt.todolist_test.domain.common.PageRequest pageRequest) {
        var springPageable = toSpringPageable(pageRequest);
        Page<TodoJpaEntity> result = jpaRepository.findByListIdAndParentIdIsNull(listId.value(), springPageable);
        List<Todo> content = result.getContent().stream().map(mapper::toDomain).toList();
        return PageResult.of(content, pageRequest.page(), pageRequest.size(), result.getTotalElements());
    }

    @Override
    public List<Todo> findChildrenOf(TodoId parentId) {
        return jpaRepository.findByParentId(parentId.value())
                .stream().map(mapper::toDomain).toList();
    }


    @Override
    public List<Todo> findAllDescendants(TodoId id) {
        return jpaRepository.findAllDescendants(id.value())
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Todo> findAllRootsByListId(TodoListId listId) {
        return jpaRepository.findByListIdAndParentIdIsNull(listId.value())
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public PageResult<Todo> search(TodoListId listId, String keyword, TodoStatus status,
                                   com.dotnt.todolist_test.domain.common.PageRequest pageRequest) {
        String statusStr = status != null ? status.name() : null;
        var springPageable = toSpringPageable(pageRequest);
        Page<TodoJpaEntity> result = jpaRepository.search(listId.value(), keyword, statusStr, springPageable);
        List<Todo> content = result.getContent().stream().map(mapper::toDomain).toList();
        return PageResult.of(content, pageRequest.page(), pageRequest.size(), result.getTotalElements());
    }

    @Override
    public int countRootsByListId(TodoListId listId) {
        return (int) jpaRepository.countByListIdAndParentIdIsNull(listId.value());
    }

    @Override
    public int countChildrenOf(TodoId parentId) {
        return (int) jpaRepository.countByParentId(parentId.value());
    }

    @Override
    public void delete(Todo todo) {
        jpaRepository.deleteById(todo.getId().value());
    }

    @Override
    public boolean existsById(TodoId id) {
        return jpaRepository.existsById(id.value());
    }
    private org.springframework.data.domain.Pageable toSpringPageable(com.dotnt.todolist_test.domain.common.PageRequest pageRequest) {
        Sort.Direction direction = pageRequest.direction() == com.dotnt.todolist_test.domain.common.SortDirection.DESC
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, pageRequest.sortBy());
        return org.springframework.data.domain.PageRequest.of(pageRequest.page(), pageRequest.size(), sort);
    }
}
