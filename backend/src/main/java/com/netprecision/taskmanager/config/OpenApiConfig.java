package com.netprecision.taskmanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI taskManagerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Manager API")
                        .version("1.0.0")
                        .description("API REST para gerenciamento de tarefas, desenvolvida com Spring Boot, DDD, JPA e H2.")
                        .contact(new Contact()
                                .name("NetPrecision Back-End")
                                .url("https://github.com/matheusmoreirap852/netPrecisionBack-End"))
                        .license(new License().name("MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Ambiente local"),
                        new Server().url("http://localhost:8082").description("Ambiente Docker alternativo")
                ));
    }
}
