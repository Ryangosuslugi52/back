package com.example.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record UpdateTaskRequest(
        String title,
        String description,
        String assigneeId
) {}