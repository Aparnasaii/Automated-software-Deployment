package com.deploymentpipeline.repository;

import com.deploymentpipeline.model.Task;
import com.deploymentpipeline.model.TaskStatus;
import com.deploymentpipeline.model.TaskPriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByPriority(TaskPriority priority);

    List<Task> findByTitleContainingIgnoreCase(String keyword);
}
