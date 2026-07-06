package com.dotnt.todolist_test.infracstructure.persistence.repository;

import com.dotnt.todolist_test.infracstructure.persistence.entity.TodoJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
public interface TodoJpaRepository extends JpaRepository<TodoJpaEntity, String> {

    Page<TodoJpaEntity> findByListIdAndParentIdIsNull(String listId, Pageable pageable);

    List<TodoJpaEntity> findByListIdAndParentIdIsNull(String listId);

    List<TodoJpaEntity> findByParentId(String parentId);

    long countByListIdAndParentIdIsNull(String listId);

    long countByParentId(String parentId);

    @Query(value = """
            WITH RECURSIVE descendants AS (
                SELECT * FROM todo WHERE parent_id = :rootId
                UNION ALL
                SELECT t.* FROM todo t
                INNER JOIN descendants d ON t.parent_id = d.id
            )
            SELECT * FROM descendants
            """, nativeQuery = true)
    List<TodoJpaEntity> findAllDescendants(@Param("rootId") String rootId);

    @Query("""
            SELECT t FROM TodoJpaEntity t
            WHERE t.listId = :listId
              AND (:keyword IS NULL OR LOWER(t.content) LIKE LOWER(CONCAT('%', :keyword, '%')))
              AND (:status IS NULL OR t.status = :status)
            """)
    Page<TodoJpaEntity> search(@Param("listId") String listId,
                               @Param("keyword") String keyword,
                               @Param("status") String status,
                               Pageable pageable);
}
