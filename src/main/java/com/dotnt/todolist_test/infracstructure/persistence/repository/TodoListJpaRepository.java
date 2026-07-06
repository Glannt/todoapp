package com.dotnt.todolist_test.infracstructure.persistence.repository;

import com.dotnt.todolist_test.infracstructure.persistence.entity.TodoListJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public interface TodoListJpaRepository extends JpaRepository<TodoListJpaEntity, String> {
}
