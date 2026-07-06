package com.dotnt.todolist_test.presentation.controller;

import com.dotnt.todolist_test.application.dto.command.CreateTodoCommand;
import com.dotnt.todolist_test.application.dto.command.MoveTodoCommand;
import com.dotnt.todolist_test.application.dto.command.UpdateTodoCommand;
import com.dotnt.todolist_test.application.dto.query.TodoView;
import com.dotnt.todolist_test.application.usecase.todo.*;
import com.dotnt.todolist_test.domain.common.PageResult;
import com.dotnt.todolist_test.presentation.request.CreateTodoRequest;
import com.dotnt.todolist_test.presentation.request.MoveTodoRequest;
import com.dotnt.todolist_test.presentation.request.UpdateTodoRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final CreateTodoUseCase createTodoUseCase;
    private final UpdateTodoUseCase updateTodoUseCase;
    private final DeleteTodoUseCase deleteTodoUseCase;
    private final ToggleTodoStatusUseCase toggleTodoStatusUseCase;
    private final GetTodoTreeUseCase getTodoTreeUseCase;
    private final SearchTodoUseCase searchTodoUseCase;
    private final MoveTodoUseCase moveTodoUseCase;

    public TodoController(CreateTodoUseCase createTodoUseCase,
                          UpdateTodoUseCase updateTodoUseCase,
                          DeleteTodoUseCase deleteTodoUseCase,
                          ToggleTodoStatusUseCase toggleTodoStatusUseCase,
                          GetTodoTreeUseCase getTodoTreeUseCase,
                          SearchTodoUseCase searchTodoUseCase,
                          MoveTodoUseCase moveTodoUseCase) {
        this.createTodoUseCase = createTodoUseCase;
        this.updateTodoUseCase = updateTodoUseCase;
        this.deleteTodoUseCase = deleteTodoUseCase;
        this.toggleTodoStatusUseCase = toggleTodoStatusUseCase;
        this.getTodoTreeUseCase = getTodoTreeUseCase;
        this.searchTodoUseCase = searchTodoUseCase;
        this.moveTodoUseCase = moveTodoUseCase;
    }

    @GetMapping
    public ResponseEntity<PageResult<TodoView>> getTree(
            @RequestParam String listId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        return ResponseEntity.ok(getTodoTreeUseCase.execute(listId, page, size, sortBy, sortDir));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResult<TodoView>> search(
            @RequestParam String listId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        return ResponseEntity.ok(searchTodoUseCase.execute(listId, keyword, status, page, size, sortBy, sortDir));
    }

    @PostMapping
    public ResponseEntity<TodoView> create(@RequestBody @Valid CreateTodoRequest request) {
        CreateTodoCommand command = new CreateTodoCommand(
                request.listId(), request.content(), request.parentId());
        return ResponseEntity.ok(createTodoUseCase.execute(command));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoView> update(@PathVariable String id,
                                           @RequestBody @Valid UpdateTodoRequest request) {
        UpdateTodoCommand command = new UpdateTodoCommand(request.content());
        return ResponseEntity.ok(updateTodoUseCase.execute(id, command));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        deleteTodoUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TodoView> toggle(@PathVariable String id) {
        return ResponseEntity.ok(toggleTodoStatusUseCase.execute(id));
    }

    @PatchMapping("/{id}/move")
    public ResponseEntity<TodoView> move(@PathVariable String id,
                                         @RequestBody MoveTodoRequest request) {
        MoveTodoCommand command = new MoveTodoCommand(id, request.newParentId());
        return ResponseEntity.ok(moveTodoUseCase.execute(command));
    }
}
