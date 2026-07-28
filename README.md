# Task Manager API

Backend do teste pratico Fullstack Angular/Spring: uma API REST para gerenciar tarefas.

## Stack

- Java 17
- Spring Boot 4.1.0
- Spring Web MVC
- Spring Data JPA
- Bean Validation
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
