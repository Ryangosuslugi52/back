package com.example.repository;

import com.example.domain.Task;
import com.example.domain.TaskStatus;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.PageableRepository;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends PageableRepository<Task, UUID> {
    Page<Task> findByProjectId(UUID projectId, Pageable pageable);
    Page<Task> findByProjectIdAndStatus(UUID projectId, TaskStatus status, Pageable pageable);
    Page<Task> findByStatus(TaskStatus status, Pageable pageable);
    List<Task> findByProjectId(UUID projectId);
}