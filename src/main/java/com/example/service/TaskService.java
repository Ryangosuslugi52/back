package com.example.service;

import com.example.domain.Task;
import com.example.domain.TaskStatus;
import com.example.dto.BoardResponse;
import com.example.dto.PaginatedTasksResponse;
import com.example.repository.TaskRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Collections;

@Singleton
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task) {
        task.setStatus(TaskStatus.TODO);
        task.setNumber("TASK-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase());
        return taskRepository.save(task);
    }

    @Transactional
    public PaginatedTasksResponse getPaginatedTasks(UUID projectId, TaskStatus status, int page, int limit) {
        Pageable pageable = Pageable.from(page - 1, limit);
        Page<Task> taskPage;

        if (projectId != null && status != null) {
            taskPage = taskRepository.findByProjectIdAndStatus(projectId, status, pageable);
        } else if (projectId != null) {
            taskPage = taskRepository.findByProjectId(projectId, pageable);
        } else if (status != null) {
            taskPage = taskRepository.findByStatus(status, pageable);
        } else {
            taskPage = taskRepository.findAll(pageable);
        }

        return new PaginatedTasksResponse(taskPage.getContent(), page, limit, taskPage.getTotalSize());
    }

    public Optional<Task> getTaskById(UUID taskId) {
        return taskRepository.findById(taskId);
    }

    public Optional<Task> updateTask(UUID taskId, Task updatedData) {
        return taskRepository.findById(taskId).map(existingTask -> {
            if (updatedData.getTitle() != null) existingTask.setTitle(updatedData.getTitle());
            if (updatedData.getDescription() != null) existingTask.setDescription(updatedData.getDescription());
            if (updatedData.getAssignee() != null) existingTask.setAssignee(updatedData.getAssignee());
            return taskRepository.update(existingTask);
        });
    }

    public Optional<Task> changeStatus(UUID taskId, TaskStatus newStatus) {
        return taskRepository.findById(taskId).map(task -> {
            task.setStatus(newStatus);
            return taskRepository.update(task);
        });
    }

    public BoardResponse getBoardForProject(UUID projectId) {
        List<Task> tasks = taskRepository.findByProjectId(projectId);
        Map<String, List<Task>> groupedTasks = tasks.stream()
                .collect(Collectors.groupingBy(task -> task.getStatus().name()));

        for (TaskStatus status : TaskStatus.values()) {
            groupedTasks.putIfAbsent(status.name(), Collections.emptyList());
        }

        return new BoardResponse(projectId.toString(), groupedTasks);
    }
}