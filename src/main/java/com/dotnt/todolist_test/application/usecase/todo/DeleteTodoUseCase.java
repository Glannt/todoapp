package com.dotnt.todolist_test.application.usecase.todo;

import com.dotnt.todolist_test.domain.exception.TodoNotFoundException;
import com.dotnt.todolist_test.domain.model.todo.Todo;
import com.dotnt.todolist_test.domain.model.todo.TodoId;
import com.dotnt.todolist_test.domain.repository.TodoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class DeleteTodoUseCase {

    private final TodoRepository todoRepository;

    public DeleteTodoUseCase(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Transactional
    public void execute(String id) {
        TodoId todoId = TodoId.of(id);
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new TodoNotFoundException(id));

        List<Todo> descendants = new ArrayList<>(todoRepository.findAllDescendants(todoId));
        descendants.sort(Comparator.comparingInt(this::depthOf).reversed());
        descendants.forEach(todoRepository::delete);

        todoRepository.delete(todo);
    }

    private int depthOf(Todo todo) {
        int depth = 0;
        TodoId currentParent = todo.getParentId();
        while (currentParent != null) {
            depth++;
            var parentOpt = todoRepository.findById(currentParent);
            if (parentOpt.isEmpty()) break;
            currentParent = parentOpt.get().getParentId();
        }
        return depth;
    }
}
