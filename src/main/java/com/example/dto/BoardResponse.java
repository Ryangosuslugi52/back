package com.example.dto;

import com.example.domain.Task;
import io.micronaut.serde.annotation.Serdeable;
import java.util.List;
import java.util.Map;

@Serdeable
public class BoardResponse {
    private String projectId;
    private Map<String, List<Task>> columns;

    public BoardResponse(String projectId, Map<String, List<Task>> columns) {
        this.projectId = projectId;
        this.columns = columns;
    }

    public String getProjectId() {
        return projectId;
    }

    public Map<String, List<Task>> getColumns() {
        return columns;
    }
}