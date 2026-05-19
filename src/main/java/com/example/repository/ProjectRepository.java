package com.example.repository;

import com.example.domain.Project;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends CrudRepository<Project, UUID> {
    @Override
    List<Project> findAll();
}