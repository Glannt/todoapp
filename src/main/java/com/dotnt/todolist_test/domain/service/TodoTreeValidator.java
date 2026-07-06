package com.dotnt.todolist_test.domain.service;

import com.dotnt.todolist_test.domain.exception.CircularReferenceException;
import com.dotnt.todolist_test.domain.model.todo.Todo;
import com.dotnt.todolist_test.domain.model.todo.TodoId;
import com.dotnt.todolist_test.domain.model.todoList.TodoListId;
import com.dotnt.todolist_test.domain.repository.TodoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TodoTreeValidator {

    private final TodoRepository todoRepository;

    public TodoTreeValidator(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public void validateNoCycle(TodoId todoId, TodoId newParentId){
        if(newParentId == null){
            return;
        }
        if(newParentId.equals(todoId)){
            throw new CircularReferenceException("Không thể tự làm cha của chính mình");
        }
        List<Todo> descendants = todoRepository.findAllDescendants(todoId);
        boolean createsCycle = descendants.stream().anyMatch(descendant -> descendant.getId().equals(newParentId));
        if(createsCycle){
            throw new CircularReferenceException("Không thể chuyển task vào chính con của nó");
        }
    }
}
