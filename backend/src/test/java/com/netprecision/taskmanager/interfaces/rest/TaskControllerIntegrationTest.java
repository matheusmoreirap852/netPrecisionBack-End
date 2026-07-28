package com.netprecision.taskmanager.interfaces.rest;

import static org.assertj.core.api.Assertions.assertThat;

import com.netprecision.taskmanager.application.dto.TaskResponse;
import com.netprecision.taskmanager.interfaces.rest.dto.CreateTaskRequest;
import com.netprecision.taskmanager.interfaces.rest.dto.UpdateTaskStatusRequest;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class TaskControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateListUpdateAndDeleteTask() {
        ResponseEntity<TaskResponse> createdResponse = restTemplate.postForEntity(
                "/api/tasks",
                new CreateTaskRequest("Build API", "DDD task manager"),
                TaskResponse.class
        );

        assertThat(createdResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        TaskResponse created = Objects.requireNonNull(createdResponse.getBody());
        assertThat(created.id()).isNotNull();
        assertThat(created.completed()).isFalse();

        ResponseEntity<TaskResponse[]> listResponse = restTemplate.getForEntity("/api/tasks", TaskResponse[].class);
        assertThat(listResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(listResponse.getBody()))
                .extracting(TaskResponse::title)
                .contains("Build API");

        ResponseEntity<TaskResponse> updatedResponse = restTemplate.exchange(
                "/api/tasks/" + created.id() + "/status",
                HttpMethod.PATCH,
                new HttpEntity<>(new UpdateTaskStatusRequest(true)),
                TaskResponse.class
        );

        assertThat(updatedResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(updatedResponse.getBody()).completed()).isTrue();

        ResponseEntity<Void> deletedResponse = restTemplate.exchange(
                "/api/tasks/" + created.id(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class
        );

        assertThat(deletedResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
