package com.dotnt.todolist_test.application.usecase.todolist;

import com.dotnt.todolist_test.domain.exception.TodoListNotFoundException;
import com.dotnt.todolist_test.domain.model.todo.Todo;
import com.dotnt.todolist_test.domain.model.todoList.TodoList;
import com.dotnt.todolist_test.domain.model.todoList.TodoListId;
import com.dotnt.todolist_test.domain.repository.TodoListRepository;
import com.dotnt.todolist_test.domain.repository.TodoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeleteTodoListUseCase {

    private final TodoListRepository todoListRepository;
    private final TodoRepository todoRepository;

    public DeleteTodoListUseCase(TodoListRepository todoListRepository, TodoRepository todoRepository) {
        this.todoListRepository = todoListRepository;
        this.todoRepository = todoRepository;
    }

    @Transactional
    public void execute(String id) {
        TodoListId listId = TodoListId.of(id);
        TodoList todoList = todoListRepository.findById(listId)
                .orElseThrow(() -> new TodoListNotFoundException(id));

        // Dùng findAllRootsByListId (KHÔNG phân trang) vì cần xóa HẾT, không phải chỉ 1 trang
        List<Todo> roots = todoRepository.findAllRootsByListId(listId);
        for (Todo root : roots) {
            todoRepository.findAllDescendants(root.getId()).forEach(todoRepository::delete);
            todoRepository.delete(root);
        }

        todoListRepository.delete(todoList);
    }
}
