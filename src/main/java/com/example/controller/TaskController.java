package com.example.controller;

import com.example.domain.Task;
import com.example.domain.TaskStatus;
import com.example.domain.User;
import com.example.dto.PaginatedTasksResponse;
import com.example.dto.CreateTaskRequest;
import com.example.dto.UpdateTaskRequest;
import com.example.dto.ChangeStatusRequest;
import com.example.service.TaskService;
import com.example.service.UserService;
import com.example.service.ProjectService;
import io.micronaut.http.annotation.*;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.core.annotation.Nullable;
import jakarta.transaction.Transactional;

import java.util.UUID;

@Controller("/v1/tasks") // 🔥 Вернули главную аннотацию
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;
    private final ProjectService projectService;

    public TaskController(TaskService taskService, UserService userService, ProjectService projectService) {
        this.taskService = taskService;
        this.userService = userService;
        this.projectService = projectService;
    }

    @Get
    @Transactional
    public PaginatedTasksResponse list(
            @Nullable @QueryValue UUID projectId, // Очистили от мусора
            @Nullable @QueryValue TaskStatus status,
            @QueryValue(defaultValue = "1") int page,
            @QueryValue(defaultValue = "20") int limit) {
        return taskService.getPaginatedTasks(projectId, status, page, limit);
    }

    @Post
    @Status(HttpStatus.CREATED)
    public Task create(@Nullable @Header("Authorization") String authorization, @Body CreateTaskRequest request) { // 🔥 Исправили на @Header
        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());

        if (request.projectId() == null) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "ProjectId is required");
        }

        UUID projectId = UUID.fromString(request.projectId());
        task.setProject(projectService.findById(projectId)
                .orElseThrow(() -> new HttpStatusException(HttpStatus.BAD_REQUEST, "Project not found")));

        // Вытаскиваем реального автора из токена авторизации
        User author = null;
        if (authorization != null && authorization.startsWith("Bearer fake-token-")) {
            try {
                String userIdStr = authorization.substring("Bearer fake-token-".length()).trim();
                UUID userId = UUID.fromString(userIdStr);
                author = userService.findById(userId).orElse(null);
            } catch (Exception e) {
                // Подстраховка
            }
        }

        if (author == null) {
            author = userService.getDefaultAuthor()
                    .orElseThrow(() -> new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No users in DB"));
        }
        task.setAuthor(author);

        if (request.assigneeId() != null) {
            UUID assigneeId = UUID.fromString(request.assigneeId());
            task.setAssignee(userService.findById(assigneeId).orElse(null));
        }

        return taskService.createTask(task);
    }

    @Get("/{taskId}")
    public Task getById(@PathVariable UUID taskId) {
        return taskService.getTaskById(taskId)
                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    @Put("/{taskId}") // 🔥 Вернули аннотацию обновления
    public Task update(@PathVariable UUID taskId, @Body UpdateTaskRequest request) {
        Task updatedData = new Task();
        if (request.title() != null) updatedData.setTitle(request.title());
        if (request.description() != null) updatedData.setDescription(request.description());
        if (request.assigneeId() != null) {
            UUID assigneeId = UUID.fromString(request.assigneeId());
            updatedData.setAssignee(userService.findById(assigneeId).orElse(null));
        }
        return taskService.updateTask(taskId, updatedData)
                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    @Put("/{taskId}/status") // 🔥 Вернули аннотацию изменения статуса
    public Task changeStatus(@PathVariable UUID taskId, @Body ChangeStatusRequest request) {
        try {
            TaskStatus newStatus = TaskStatus.valueOf(request.status());
            return taskService.changeStatus(taskId, newStatus)
                    .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        } catch (IllegalArgumentException e) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Invalid status value");
        }
    }
}