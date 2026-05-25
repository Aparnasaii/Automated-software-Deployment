package com.deploymentpipeline.integration;

import com.deploymentpipeline.Application;
import com.deploymentpipeline.model.Task;
import com.deploymentpipeline.model.TaskStatus;
import com.deploymentpipeline.model.TaskPriority;
import com.deploymentpipeline.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Task API Integration Tests")
class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    @Order(1)
    @DisplayName("Full CRUD lifecycle test")
    void fullCrudLifecycle() throws Exception {
        // CREATE
        Task task = new Task();
        task.setTitle("Integration Test Task");
        task.setDescription("Testing full lifecycle");
        task.setPriority(TaskPriority.HIGH);

        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Integration Test Task"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andReturn().getResponse().getContentAsString();

        Long taskId = objectMapper.readTree(response).get("id").asLong();

        // READ
        mockMvc.perform(get("/api/tasks/" + taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Integration Test Task"));

        // UPDATE
        Task update = new Task();
        update.setTitle("Updated Integration Task");
        update.setDescription("Updated description");
        update.setStatus(TaskStatus.COMPLETED);
        update.setPriority(TaskPriority.LOW);

        mockMvc.perform(put("/api/tasks/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Integration Task"))
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        // DELETE
        mockMvc.perform(delete("/api/tasks/" + taskId))
                .andExpect(status().isNoContent());

        // VERIFY DELETED
        mockMvc.perform(get("/api/tasks/" + taskId))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(2)
    @DisplayName("Filter tasks by status")
    void filterByStatus() throws Exception {
        Task pending = new Task();
        pending.setTitle("Pending Task");
        pending.setStatus(TaskStatus.PENDING);
        taskRepository.save(pending);

        Task completed = new Task();
        completed.setTitle("Completed Task");
        completed.setStatus(TaskStatus.COMPLETED);
        taskRepository.save(completed);

        mockMvc.perform(get("/api/tasks/status/PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Pending Task"));
    }

    @Test
    @Order(3)
    @DisplayName("Search tasks by keyword")
    void searchByKeyword() throws Exception {
        Task task = new Task();
        task.setTitle("Deploy Microservice");
        taskRepository.save(task);

        mockMvc.perform(get("/api/tasks/search").param("keyword", "deploy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Deploy Microservice"));
    }

    @Test
    @Order(4)
    @DisplayName("Health check endpoint")
    void healthCheck() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.application").value("Automated Deployment System"));
    }
}
