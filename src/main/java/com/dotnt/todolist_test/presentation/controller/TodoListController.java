package com.dotnt.todolist_test.presentation.controller;

import com.dotnt.todolist_test.application.dto.command.CreateTodoListCommand;
import com.dotnt.todolist_test.application.dto.command.UpdateTodoListCommand;
import com.dotnt.todolist_test.application.dto.query.TodoListView;
import com.dotnt.todolist_test.application.usecase.todolist.CreateTodoListUseCase;
import com.dotnt.todolist_test.application.usecase.todolist.DeleteTodoListUseCase;
import com.dotnt.todolist_test.application.usecase.todolist.GetAllTodoListsUseCase;
import com.dotnt.todolist_test.application.usecase.todolist.UpdateTodoListUseCase;
import com.dotnt.todolist_test.presentation.request.CreateTodoListRequest;
import com.dotnt.todolist_test.presentation.request.UpdateTodoListRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo-lists")
public class TodoListController {

    private final CreateTodoListUseCase createTodoListUseCase;
    private final UpdateTodoListUseCase updateTodoListUseCase;
    private final DeleteTodoListUseCase deleteTodoListUseCase;
    private final GetAllTodoListsUseCase getAllTodoListsUseCase;

    public TodoListController(CreateTodoListUseCase createTodoListUseCase,
                              UpdateTodoListUseCase updateTodoListUseCase,
                              DeleteTodoListUseCase deleteTodoListUseCase,
                              GetAllTodoListsUseCase getAllTodoListsUseCase) {
        this.createTodoListUseCase = createTodoListUseCase;
        this.updateTodoListUseCase = updateTodoListUseCase;
        this.deleteTodoListUseCase = deleteTodoListUseCase;
        this.getAllTodoListsUseCase = getAllTodoListsUseCase;
    }

    @GetMapping
    public ResponseEntity<List<TodoListView>> getAll() {
        return ResponseEntity.ok(getAllTodoListsUseCase.execute());
    }

    @PostMapping
    public ResponseEntity<TodoListView> create(@RequestBody @Valid CreateTodoListRequest request) {
        CreateTodoListCommand command = new CreateTodoListCommand(request.name(), request.description());
        return ResponseEntity.ok(createTodoListUseCase.execute(command));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoListView> update(@PathVariable String id,
                                               @RequestBody @Valid UpdateTodoListRequest request) {
        UpdateTodoListCommand command = new UpdateTodoListCommand(request.name(), request.description());
        return ResponseEntity.ok(updateTodoListUseCase.execute(id, command));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        deleteTodoListUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
