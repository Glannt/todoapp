package com.dotnt.todolist_test.infracstructure.persistence.impl;
import com.dotnt.todolist_test.domain.model.todoList.TodoList;
import com.dotnt.todolist_test.domain.model.todoList.TodoListId;
import com.dotnt.todolist_test.domain.repository.TodoListRepository;
import com.dotnt.todolist_test.infracstructure.persistence.entity.TodoListJpaEntity;
import com.dotnt.todolist_test.infracstructure.persistence.mapper.TodoListPersistenceMapper;
import com.dotnt.todolist_test.infracstructure.persistence.repository.TodoListJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TodoListRepositoryImpl implements TodoListRepository {

    private final TodoListJpaRepository jpaRepository;
    private final TodoListPersistenceMapper mapper;

    public TodoListRepositoryImpl(TodoListJpaRepository jpaRepository, TodoListPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public TodoList save(TodoList todoList) {
        TodoListJpaEntity saved = jpaRepository.save(mapper.toJpa(todoList));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<TodoList> findById(TodoListId id) {
        return jpaRepository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public List<TodoList> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void delete(TodoList todoList) {
        jpaRepository.deleteById(todoList.getId().value());
    }

    @Override
    public boolean existsById(TodoListId id) {
        return jpaRepository.existsById(id.value());
    }
}
