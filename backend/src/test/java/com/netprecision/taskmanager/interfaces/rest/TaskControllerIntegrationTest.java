package com.netprecision.taskmanager.interfaces.rest;

import static org.assertj.core.api.Assertions.assertThat;

import com.netprecision.taskmanager.application.dto.AuthResponse;
import com.netprecision.taskmanager.application.dto.TaskResponse;
import com.netprecision.taskmanager.interfaces.rest.dto.CreateTaskRequest;
import com.netprecision.taskmanager.interfaces.rest.dto.RegisterRequest;
import com.netprecision.taskmanager.interfaces.rest.dto.UpdateTaskStatusRequest;
import java.util.Objects;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
        HttpHeaders headers = authHeaders();
        HttpEntity<CreateTaskRequest> createRequest = new HttpEntity<>(
                new CreateTaskRequest("Build API", "DDD task manager"),
                headers
        );

        ResponseEntity<TaskResponse> createdResponse = restTemplate.postForEntity(
                "/api/tasks",
                createRequest,
                TaskResponse.class
        );

        assertThat(createdResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        TaskResponse created = Objects.requireNonNull(createdResponse.getBody());
        assertThat(created.id()).isNotNull();
        assertThat(created.completed()).isFalse();

        ResponseEntity<TaskResponse[]> listResponse = restTemplate.exchange(
                "/api/tasks",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                TaskResponse[].class
        );
        assertThat(listResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(listResponse.getBody()))
                .extracting(TaskResponse::title)
                .contains("Build API");

        ResponseEntity<TaskResponse> foundResponse = restTemplate.exchange(
                "/api/tasks/" + created.id(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                TaskResponse.class
        );
        assertThat(foundResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(foundResponse.getBody()).title()).isEqualTo("Build API");

        ResponseEntity<TaskResponse> updatedResponse = restTemplate.exchange(
                "/api/tasks/" + created.id() + "/status",
                HttpMethod.PATCH,
                new HttpEntity<>(new UpdateTaskStatusRequest(true), headers),
                TaskResponse.class
        );

        assertThat(updatedResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(updatedResponse.getBody()).completed()).isTrue();

        ResponseEntity<Void> deletedResponse = restTemplate.exchange(
                "/api/tasks/" + created.id(),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class
        );

        assertThat(deletedResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void shouldExposeOpenApiDocumentation() {
        ResponseEntity<String> docsResponse = restTemplate.getForEntity("/v3/api-docs", String.class);

        assertThat(docsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(docsResponse.getBody())
                .contains("\"title\":\"Task Manager API\"")
                .contains("\"url\":\"/\"")
                .contains("\"201\"")
                .contains("/api/tasks")
                .contains("/api/tasks/{id}/status");
    }

    private HttpHeaders authHeaders() {
        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                "/api/auth/register",
                new RegisterRequest("Test User", "user-" + UUID.randomUUID() + "@example.com", "secret123"),
                AuthResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        String token = Objects.requireNonNull(response.getBody()).token();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }
}
