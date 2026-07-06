package com.dotnt.todolist_test.application.usecase.todo;

import com.dotnt.todolist_test.application.dto.command.CreateTodoCommand;
import com.dotnt.todolist_test.application.dto.query.TodoView;
import com.dotnt.todolist_test.domain.exception.TodoListNotFoundException;
import com.dotnt.todolist_test.domain.exception.TodoNotFoundException;
import com.dotnt.todolist_test.domain.model.todo.Todo;
import com.dotnt.todolist_test.domain.model.todo.TodoContent;
import com.dotnt.todolist_test.domain.model.todo.TodoId;
import com.dotnt.todolist_test.domain.model.todoList.TodoListId;
import com.dotnt.todolist_test.domain.repository.TodoListRepository;
import com.dotnt.todolist_test.domain.repository.TodoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateTodoUseCase {
    private final TodoRepository todoRepository;
    private final TodoListRepository todoListRepository;

    public CreateTodoUseCase(TodoRepository todoRepository, TodoListRepository todoListRepository) {
        this.todoRepository = todoRepository;
        this.todoListRepository = todoListRepository;
    }

    @Transactional
    public TodoView execute(CreateTodoCommand command) {
        TodoListId listId = TodoListId.of(command.listId());
        if (!todoListRepository.existsById(listId)) {
            throw new TodoListNotFoundException(command.listId());
        }

        TodoContent content = TodoContent.of(command.content());
        Todo todo;
        System.out.println("CreateTodoUseCase: command.parentId() = " + command.parentId());

        if (command.parentId() == null || command.parentId().isBlank()) {
            int nextOrder = todoRepository.countRootsByListId(listId);
            todo = Todo.createRoot(content, listId, nextOrder);
        } else {
            TodoId parentId = TodoId.of(command.parentId());
            if (!todoRepository.existsById(parentId)) {
                throw new TodoNotFoundException(command.parentId());
            }
            int nextOrder = todoRepository.countChildrenOf(parentId);
            todo = Todo.createChild(content, listId, parentId, nextOrder);
        }

        return TodoView.fromFlat(todoRepository.save(todo));
    }
}
