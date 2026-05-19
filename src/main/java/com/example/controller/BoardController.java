package com.example.controller;

import com.example.dto.BoardResponse;
import com.example.service.TaskService;
import io.micronaut.http.annotation.*;
import java.util.UUID;

@Controller("/v1/board")
public class BoardController {
    private final TaskService taskService;

    public BoardController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Get
    public BoardResponse getBoardByQuery(@QueryValue UUID projectId) {
        return taskService.getBoardForProject(projectId);
    }

    @Get("/{id}")
    public BoardResponse getBoardByPath(@PathVariable UUID id) {
        return taskService.getBoardForProject(id);
    }
}