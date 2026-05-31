package com.example.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record ChangeStatusRequest(
        String status
) {}