package com.example.controller;

import com.example.domain.Project;
import com.example.service.ProjectService;
import io.micronaut.http.annotation.*;
import io.micronaut.http.HttpStatus;
import java.util.List;

@Controller("/v1/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Post
    @Status(HttpStatus.CREATED)
    public Project create(@Body Project project) {
        return projectService.createProject(project);
    }

    @Get
    public List<Project> list() {
        return projectService.getAllProjects();
    }
}