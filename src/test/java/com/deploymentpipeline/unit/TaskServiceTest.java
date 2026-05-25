package com.deploymentpipeline.unit;

import com.deploymentpipeline.model.Task;
import com.deploymentpipeline.model.TaskStatus;
import com.deploymentpipeline.model.TaskPriority;
import com.deploymentpipeline.repository.TaskRepository;
import com.deploymentpipeline.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Task Service Unit Tests")
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task sampleTask;

    @BeforeEach
    void setUp() {
        sampleTask = new Task();
        sampleTask.setId(1L);
        sampleTask.setTitle("Test Task");
        sampleTask.setDescription("Test Description");
        sampleTask.setStatus(TaskStatus.PENDING);
        sampleTask.setPriority(TaskPriority.HIGH);
    }

    @Test
    @DisplayName("Should return all tasks")
    void getAllTasks_ShouldReturnAllTasks() {
        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Second Task");

        when(taskRepository.findAll()).thenReturn(Arrays.asList(sampleTask, task2));

        List<Task> result = taskService.getAllTasks();

        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return task by ID when exists")
    void getTaskById_WhenExists_ShouldReturnTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));

        Optional<Task> result = taskService.getTaskById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Task", result.get().getTitle());
    }

    @Test
    @DisplayName("Should return empty when task not found")
    void getTaskById_WhenNotExists_ShouldReturnEmpty() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Task> result = taskService.getTaskById(99L);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should create a new task")
    void createTask_ShouldSaveAndReturnTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);

        Task result = taskService.createTask(sampleTask);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Should update existing task")
    void updateTask_WhenExists_ShouldUpdateAndReturn() {
        Task updatedData = new Task();
        updatedData.setTitle("Updated Title");
        updatedData.setDescription("Updated Description");
        updatedData.setStatus(TaskStatus.COMPLETED);
        updatedData.setPriority(TaskPriority.LOW);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);

        Optional<Task> result = taskService.updateTask(1L, updatedData);

        assertTrue(result.isPresent());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Should return false when deleting non-existent task")
    void deleteTask_WhenNotExists_ShouldReturnFalse() {
        when(taskRepository.existsById(99L)).thenReturn(false);

        boolean result = taskService.deleteTask(99L);

        assertFalse(result);
        verify(taskRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should delete task and return true")
    void deleteTask_WhenExists_ShouldReturnTrue() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        boolean result = taskService.deleteTask(1L);

        assertTrue(result);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should filter tasks by status")
    void getTasksByStatus_ShouldReturnFilteredTasks() {
        when(taskRepository.findByStatus(TaskStatus.PENDING))
                .thenReturn(List.of(sampleTask));

        List<Task> result = taskService.getTasksByStatus(TaskStatus.PENDING);

        assertEquals(1, result.size());
        assertEquals(TaskStatus.PENDING, result.get(0).getStatus());
    }

    @Test
    @DisplayName("Should search tasks by keyword")
    void searchTasks_ShouldReturnMatchingTasks() {
        when(taskRepository.findByTitleContainingIgnoreCase("Test"))
                .thenReturn(List.of(sampleTask));

        List<Task> result = taskService.searchTasks("Test");

        assertEquals(1, result.size());
        assertTrue(result.get(0).getTitle().contains("Test"));
    }
}
