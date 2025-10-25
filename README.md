# Task Manager API

## Overview

The **Task Manager API** is a Spring Boot-based RESTful service that allows users to create, read, update, and delete
tasks.  
It provides endpoints for managing tasks with attributes such as title, description, completion status, and timestamps
for creation and updates.

Key features include:

- **CRUD operations** for tasks (`/tasks` endpoints)
- **Input validation** using `@Valid` and `@NotNull`
- **Global exception handling** via a centralized `GlobalExceptionHandler`
- **Integration with MySQL** as the persistent datastore, deployable via Docker
- **Comprehensive testing** with unit and integration tests, achieving over 80% code coverage
- **CI/CD workflow** configured with GitHub Actions for automated building, testing, and deployment

This API is designed to be modular, testable, and maintainable, enabling rapid development while following
enterprise-level best practices.

---

## Architecture

The system follows a **layered architecture** with clear separation of concerns:

1. **Controller Layer**
    - Handles HTTP requests and responses.
    - Uses Spring’s `@RestController` and OpenAPI-generated interfaces for type-safe API definitions.
    - Delegates business logic to the service layer.

2. **Service Layer**
    - Contains core business logic for task management.
    - Each service method is transactional where appropriate, ensuring data consistency.
    - Example: `TaskService` performs entity validation, mapping, and persistence operations.

3. **Repository Layer**
    - Based on **Spring Data JPA**, repositories interact with MySQL.
    - Provides standard CRUD operations with minimal boilerplate.

4. **Entity and DTO Mapping**
    - Entities represent database tables, while DTOs expose API-friendly models.
    - Mapping is handled via `TaskMapper`, decoupling persistence from API representation.

5. **Exception Handling**
    - `GlobalExceptionHandler` centralizes handling of:
        - Validation errors (`MethodArgumentNotValidException`)
        - Entity not found (`EntityNotFoundException`)
        - Database errors (`DataAccessException`)
        - General runtime exceptions
    - Returns consistent JSON error responses with proper HTTP status codes.

6. **Testing**
    - **Unit Tests**: Validate individual service methods using Mockito.
    - **Integration Tests**: Use Spring Boot Test and Testcontainers to validate endpoints with a real MySQL instance.
    - **Coverage**: Jacoco ensures at least 80% code coverage, with exclusions for generated OpenAPI code.

7. **Deployment**
    - **Docker Compose** is used for local development, spinning up MySQL and the Task Manager service.
    - CI/CD pipelines via GitHub Actions automate build, test, and deployment processes.
    - The pipeline integrates with the one-click startup script, ensuring consistency between local and CI environments.

**Design Patterns Used:**

- **Layered Architecture / Separation of Concerns**: Controllers → Services → Repositories → Entities
- **Builder Pattern**: Used in entity creation with `@Builder` to simplify object construction
- **Template Method**: In exception handling, the `GlobalExceptionHandler` defines a template for generating consistent
  error responses
- **Dependency Injection**: Spring’s `@Autowired` and constructor injection ensure testable, loosely-coupled components

---

## Prerequisites

Before running the Task Manager API, ensure the following are installed and available on your system:

- **Java Development Kit (JDK)**
    - Version: 21 (tested with Temurin/OpenJDK)
    - Ensure `JAVA_HOME` is set and `java`/`javac` are in your system PATH

- **Gradle**
    - Used for building the project
    - You can use the Gradle wrapper included in the project (`./gradlew`)

- **Docker**
    - Required to run the MySQL container for local development and testing
    - Version: Docker Desktop 20.x or higher
    - Ensure Docker daemon is running

- **Docker Compose**
    - Used for orchestrating MySQL and optionally other services
    - Version: 1.29+ (if using `docker-compose` CLI) or integrated in Docker Desktop

- **Git**
    - For version control and integration with CI/CD workflows

Optional:

- **IDE** (e.g., IntelliJ IDEA, VS Code) for development, debugging, and running tests
- **cURL or Postman** for testing API endpoints

---

## Quick Start

```bash
# Single command to run the entire application
./run.sh
# Ctrl + C to shutdown services in the terminal
```

---

## API Documentation

- **OpenAPI Specification**: Available at `/swagger-ui.html` when running (code generation with
  `./gradlew openApiGenerate`)
- **Postman Collection**: Import `postman_collection.json` for testing

---

## Testing

Describe your testing strategy and how to run tests.

### Unit Tests

Manually run `TaskServiceTest.java / GlobalExceptionHandlerTest.java / TaskManagerApplicationTests.java` to do unit
testing.

### Integration Tests

Before you do integration tests, please ensure you have run up the docker desktop.
Then manually run `TaskApiIntegrationTest.java` to do integration testing.

---

## Database Schema

The Task Manager API uses a MySQL database to persist task data. The database structure is designed to be simple and
normalized for CRUD operations on tasks.

### Tables

#### `tasks`

| Column        | Type                 | Constraints                 | Description                      |
|---------------|----------------------|-----------------------------|----------------------------------|
| `id`          | BIGINT               | PRIMARY KEY, AUTO_INCREMENT | Unique identifier for each task  |
| `title`       | VARCHAR(255)         | NOT NULL                    | Task title                       |
| `description` | TEXT                 | NULL                        | Detailed description of the task |
| `completed`   | BOOLEAN              | NOT NULL, DEFAULT FALSE     | Task completion status           |
| `created_at`  | TIMESTAMP / DATETIME | NOT NULL                    | Task creation timestamp          |
| `updated_at`  | TIMESTAMP / DATETIME | NULL                        | Last update timestamp            |

### Database Initialization & Migration

- During development, the database is automatically created using a SQL script `createdb.sql`
- The `docker-compose.yml` includes a MySQL container configuration to facilitate easy setup.
- For production, migrations can be managed using Flyway or Liquibase to version control schema changes.
- Each table and column is designed to match the entity classes (`TaskEntity`) in the application to ensure seamless
  mapping via JPA.

### Example SQL Initialization (`createdb.sql`)

```sql
CREATE
    DATABASE IF NOT EXISTS taskmanager;
USE
    taskmanager;

CREATE TABLE IF NOT EXISTS tasks
(
    id
                BIGINT
        AUTO_INCREMENT
        PRIMARY
            KEY,
    title
                VARCHAR(255) NOT NULL,
    description TEXT,
    completed   BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NULL
);
```

---

## Observability (Not yet implemented)

- **Metrics**: Available at `/actuator/prometheus`
- **Grafana Dashboard**: Access at `http://localhost:3000`
- **Prometheus**: Access at `http://localhost:9090`

---

## CI/CD Pipeline

The Task Manager API project uses GitHub Actions for continuous integration and deployment. The CI/CD pipeline ensures
that every commit and pull request is automatically built, tested, and optionally deployed in a consistent environment.

### Workflow Overview

- **Trigger**: The pipeline is triggered on every push to the `main` branch and on pull requests.
- **Environment**: Uses `ubuntu-latest` runners provided by GitHub Actions.
- **Services**: Spins up a MySQL container for integration tests.

### Steps

1. **Checkout Code**: Pulls the latest version of the repository using `actions/checkout`.
2. **Setup JDK**: Installs Java 21 using `actions/setup-java`.
3. **Install Docker Compose**: Ensures Docker Compose is available for container management.
4. **Run One-Click Startup Script**: Executes the custom `run.sh` script, which:
    - Starts MySQL container via `docker-compose`.
    - Waits for the database to become ready.
    - Builds the Spring Boot application using Gradle.
    - Runs the application in the background.

5. **Testing**:
    - Unit tests and integration tests are executed during the Gradle build.
    - Jacoco generates a test coverage report. The build is configured to fail if coverage is below 80%.

6. **Deployment (Optional)**:
    - The `run.sh` script already handles launching the application.
    - For production deployment, the same Docker Compose configuration can be adapted to a cloud environment.

### GitHub Actions Workflow (`.github/workflows/cicd.yml`)

```yaml
name: TaskManager CI/CD Pipeline

on:
  push:
    branches:
      - main
  pull_request:

jobs:
  build-and-run:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout code
        uses: actions/checkout@v3

      - name: Set up JDK 21
        uses: actions/setup-java@v3
        with:
          distribution: temurin
          java-version: 21

      - name: Install Docker Compose
        run: sudo apt-get update && sudo apt-get install -y docker-compose

      - name: Run one-click startup script
        run: ./run.sh
```

---

## Assumptions Made

- The application will run on Java 21 and requires Docker & Docker Compose installed.
- MySQL 8.1 is used as the primary database for both development and CI/CD testing.
- The project will be deployed in a controlled environment where network access and ports (e.g., 8080, 3306) are
  available.
- Swagger/OpenAPI documentation is only for internal use and is not exposed to production without authentication.
- CI/CD assumes GitHub Actions runners with sufficient resources to run Docker and Gradle builds.

---

## Known Limitations

- Exception handling testing for `DataAccessException` and other DB errors is simulated in unit tests and cannot be
  fully verified against a live database.
- The project currently does not provide dedicated Gradle tasks setup for running unit tests (`./gradlew test`) and
  integration tests (`./gradlew integrationTest`); users need to configure these manually if needed.
- OpenAPI generated code is mostly template-based; test coverage for it is limited.
- Currently, monitoring and observability (OpenTelemetry, Prometheus, Grafana) are not fully integrated into the
  pipeline.
- No authentication or authorization mechanisms have been implemented for the API.
- The one-click startup script assumes Linux/macOS environment and may require adaptation for Windows.
- CI/CD currently runs on GitHub Actions; deploying to an actual production server or cloud platform is not automated
  yet.

## Technology Stack

- Spring Boot 3.5.x
- Java 21
- Gradle (build tool)
- MySQL 8.1
- Docker & Docker Compose
- Spring Data JPA
- SpringDoc OpenAPI (for API documentation)
- JUnit 5 (unit and integration testing)
- Mockito (mocking dependencies in tests)
- Testcontainers (integration testing with temporary MySQL container)
- JaCoCo (code coverage)
- Github Actions (I/CD workflow)

## Author

Kang Xiwen - xiwenk188@gmail.com