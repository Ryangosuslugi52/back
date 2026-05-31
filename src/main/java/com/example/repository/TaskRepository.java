package com.example.repository;

import com.example.domain.Task;
import com.example.domain.TaskStatus;
import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.PageableRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends PageableRepository<Task, UUID> {

    @Override
    @Join(value = "author", type = Join.Type.FETCH)
    @Join(value = "project", type = Join.Type.FETCH)
    Optional<Task> findById(UUID id);

    @Join(value = "author", type = Join.Type.FETCH)
    Page<Task> findByProjectId(UUID projectId, Pageable pageable);

    @Join(value = "author", type = Join.Type.FETCH)
    Page<Task> findByProjectIdAndStatus(UUID projectId, TaskStatus status, Pageable pageable);

    @Join(value = "author", type = Join.Type.FETCH)
    Page<Task> findByStatus(TaskStatus status, Pageable pageable);

    @Join(value = "author", type = Join.Type.FETCH)
    List<Task> findByProjectId(UUID projectId);

    @Override
    @Join(value = "author", type = Join.Type.FETCH)
    Page<Task> findAll(Pageable pageable);
}