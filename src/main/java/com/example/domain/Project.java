package com.example.domain;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "projects")
@Serdeable
public class Project {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}