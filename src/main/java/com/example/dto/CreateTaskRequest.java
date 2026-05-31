package com.example.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record CreateTaskRequest(
        String title,
        String description,
        String projectId,
        String assigneeId
) {}