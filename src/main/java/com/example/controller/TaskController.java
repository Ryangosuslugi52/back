package com.example.controller;

import com.example.domain.Task;
import com.example.domain.TaskStatus;
import com.example.dto.PaginatedTasksResponse;
import com.example.service.TaskService;
import com.example.service.UserService;
import com.example.service.ProjectService;
import io.micronaut.http.annotation.*;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.core.annotation.Nullable;
import java.util.Map;
import java.util.UUID;


@Controller("/v1/tasks")
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
    public PaginatedTasksResponse list(
            @Nullable @QueryValue UUID projectId,
            @Nullable @QueryValue TaskStatus status,
            @QueryValue(defaultValue = "1") int page,
            @QueryValue(defaultValue = "10") int limit) {
        return taskService.getPaginatedTasks(projectId, status, page, limit);
    }

    @Post
    @Status(HttpStatus.CREATED)
    public Task create(@Body Map<String, String> body) {
        Task task = new Task();
        task.setTitle(body.get("title"));
        task.setDescription(body.get("description"));

        UUID projectId = UUID.fromString(body.get("projectId"));
        task.setProject(projectService.findById(projectId)
                .orElseThrow(() -> new HttpStatusException(HttpStatus.BAD_REQUEST, "Project not found")));

        task.setAuthor(userService.getDefaultAuthor()
                .orElseThrow(() -> new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No users in DB")));

        if (body.get("assigneeId") != null) {
            UUID assigneeId = UUID.fromString(body.get("assigneeId"));
            task.setAssignee(userService.findById(assigneeId).orElse(null));
        }

        return taskService.createTask(task);
    }

    @Get("/{taskId}")
    public Task getById(@PathVariable UUID taskId) {
        return taskService.getTaskById(taskId)
                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    @Patch("/{taskId}")
    public Task update(@PathVariable UUID taskId, @Body Map<String, String> body) {
        Task updatedData = new Task();
        if (body.containsKey("title")) updatedData.setTitle(body.get("title"));
        if (body.containsKey("description")) updatedData.setDescription(body.get("description"));
        if (body.containsKey("assigneeId")) {
            UUID assigneeId = UUID.fromString(body.get("assigneeId"));
            updatedData.setAssignee(userService.findById(assigneeId).orElse(null));
        }

        return taskService.updateTask(taskId, updatedData)
                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    @Post("/{taskId}/status")
    public Task changeStatus(@PathVariable UUID taskId, @Body Map<String, String> body) {
        TaskStatus newStatus = TaskStatus.valueOf(body.get("status"));
        return taskService.changeStatus(taskId, newStatus)
                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }
}