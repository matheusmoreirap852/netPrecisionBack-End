# Task Manager API

Backend do teste pratico Fullstack Angular/Spring: uma API REST para gerenciar tarefas.

## Stack

- Java 17
- Spring Boot 4.1.0
- Spring Web MVC
- Spring Data JPA
- Bean Validation
- Springdoc OpenAPI / Swagger UI
- H2 Database
- JUnit 5
- Maven Wrapper

## Arquitetura

O backend foi organizado com DDD de forma simples e objetiva:

- `domain`: regras de negocio, entidade `Task`, value object `TaskTitle`, factory e porta de repositorio.
- `application`: casos de uso, DTOs de entrada/saida e o contrato generico `BaseCrudUseCase`.
- `infrastructure`: adaptadores de persistencia com Spring Data JPA e mappers.
- `interfaces`: controllers REST, `BaseController`, DTOs HTTP e tratamento global de erros.
- `config`: configuracoes transversais, como CORS.

Padroes aplicados:

- Factory: cria e restaura agregados `Task` mantendo a regra de titulo no dominio.
- Heranca: `TaskController` herda de `BaseController`, reaproveitando endpoints simples de CRUD.
- Dependency Injection: controllers e casos de uso recebem suas dependencias pelo construtor.

O objetivo e deixar a API facil de entender: a controller concreta fica pequena, a regra de negocio permanece no dominio/aplicacao, e a infraestrutura fica isolada nos adaptadores JPA.

### Fluxo da requisicao

```text
Angular -> TaskController -> TaskUseCase -> TaskRepository -> JpaTaskRepositoryAdapter -> H2
```

### Descricao da base reutilizavel

O `BaseController` concentra o CRUD comum (`GET`, `GET by id`, `POST` e `DELETE`). O `TaskController` herda essa base e implementa apenas a conversao de `CreateTaskRequest` para `CreateTaskCommand`, alem do endpoint especifico para alterar o status da tarefa.

Essa escolha mostra POO de forma simples:

- Heranca para reaproveitar comportamento comum.
- Encapsulamento para manter a regra de negocio fora da controller.
- Inversao de dependencia porque a controller chama um caso de uso, e nao acessa JPA diretamente.
- Injecao de dependencia pelo construtor, deixando as dependencias explicitas e testaveis.

## Como rodar

```bash
cd backend
./mvnw spring-boot:run
```

A API sobe em:

```text
http://localhost:8080
```

Console H2:

```text
http://localhost:8080/h2-console
```

Dados para conexao no H2:

```text
JDBC URL: jdbc:h2:mem:taskdb
User: sa
Password:
```

## Como rodar com Docker

Build da imagem:

```bash
docker build -t netprecision-task-manager-api ./backend
```

Executar container:

```bash
docker run --rm -p 8080:8080 --name netprecision-task-manager-api netprecision-task-manager-api
```

Tambem e possivel subir pela raiz do projeto com Docker Compose:

```bash
docker compose up --build
```

Para rodar em segundo plano:

```bash
docker compose up --build -d
```

Se a porta `8080` ja estiver em uso, informe outra porta para o host:

```bash
API_PORT=8082 docker compose up --build -d
```

Nesse caso a API ficara disponivel em:

```text
http://localhost:8082/api/tasks
```

Para parar:

```bash
docker compose down
```

## Swagger / OpenAPI

A documentacao da API e gerada automaticamente com Springdoc OpenAPI.

Com a aplicacao local na porta `8080`:

```text
Swagger UI: http://localhost:8080/swagger-ui.html
OpenAPI JSON: http://localhost:8080/v3/api-docs
```

Com Docker usando `API_PORT=8082`:

```text
Swagger UI: http://localhost:8082/swagger-ui.html
OpenAPI JSON: http://localhost:8082/v3/api-docs
```

O Swagger documenta os endpoints de tarefas, modelos de request/response e validacoes basicas expostas pela API.

## Testes

```bash
cd backend
./mvnw test
```

## Endpoints

### Listar tarefas

```http
GET /api/tasks
```

### Buscar tarefa por id

```http
GET /api/tasks/{id}
```

### Criar tarefa

```http
POST /api/tasks
Content-Type: application/json

{
  "title": "Criar backend",
  "description": "Implementar API com Spring Boot"
}
```

### Atualizar status

```http
PATCH /api/tasks/{id}/status
Content-Type: application/json

{
  "completed": true
}
```

### Excluir tarefa

```http
DELETE /api/tasks/{id}
```

## Exemplos com curl

```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Criar API","description":"Spring Boot + DDD"}'

curl http://localhost:8080/api/tasks

curl http://localhost:8080/api/tasks/1

curl -X PATCH http://localhost:8080/api/tasks/1/status \
  -H "Content-Type: application/json" \
  -d '{"completed":true}'

curl -X DELETE http://localhost:8080/api/tasks/1
```

## Integracao com Angular

O CORS esta liberado para:

- `http://localhost:4200`
- `http://127.0.0.1:4200`

No frontend Angular, a URL base pode apontar para:

```ts
http://localhost:8080/api/tasks
```
