# Automated Software Deployment System with Integrated Testing Pipelines

A fully automated software deployment system that integrates CI/CD pipelines, multi-level testing frameworks, containerization, real-time monitoring, and DevSecOps security practices. Built with Java Spring Boot.

---

## Table of Contents

- [Project Overview](#project-overview)
- [System Architecture](#system-architecture)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Application Features](#application-features)
- [CI/CD Pipeline](#cicd-pipeline)
- [Automated Testing](#automated-testing)
- [Containerization](#containerization)
- [Kubernetes Orchestration](#kubernetes-orchestration)
- [Monitoring & Feedback](#monitoring--feedback)
- [Security (DevSecOps)](#security-devsecops)
- [Standards Incorporated](#standards-incorporated)
- [Getting Started](#getting-started)
- [API Reference](#api-reference)
- [Screenshots](#screenshots)

---

## Project Overview

This project demonstrates an end-to-end automated deployment pipeline following DevOps best practices. When a developer pushes code, the system automatically:

1. **Builds** the application using Maven
2. **Tests** with JUnit unit tests, integration tests, and Selenium E2E tests
3. **Scans** for security vulnerabilities using SonarQube, Trivy, and OWASP
4. **Containerizes** the application using Docker
5. **Deploys** to staging and production environments via Kubernetes
6. **Monitors** application health with Prometheus and Grafana dashboards

### Objectives

- Design modular CI/CD pipelines using GitHub Actions
- Integrate automated testing frameworks (JUnit, Selenium, Postman)
- Leverage Docker and Kubernetes for scalability and portability
- Implement real-time monitoring using Prometheus and Grafana
- Incorporate DevSecOps principles with SonarQube and Trivy

---

## System Architecture

```
+------------------+       +-------------------+       +-------------------+
|   DEVELOPER      |       |   VERSION CONTROL |       |   CI/CD PIPELINE  |
|                  |       |                   |       |                   |
|  Write Code      | ----->|   GitHub          | ----->|  GitHub Actions   |
|  Write Tests     |  push |   Repository      | trigger                  |
|  Local Testing   |       |   main / develop  |       |  Build            |
+------------------+       +-------------------+       |  Test             |
                                                       |  Security Scan    |
                                                       |  Docker Build     |
                                                       +--------+----------+
                                                                |
                                                                | deploy
                                                                v
+------------------+       +-------------------+       +-------------------+
|   MONITORING     |       |   PRODUCTION      |       |   STAGING         |
|                  |       |                   |       |                   |
|  Prometheus      | <-----|   Kubernetes      | <-----|   Kubernetes      |
|  Grafana         | scrape|   Cluster (Prod)  | after |   Cluster (Stage) |
|  Alertmanager    |       |   3 Replicas      | verify|   1 Replica       |
+------------------+       +-------------------+       +-------------------+
```

### Application Architecture (3-Layer)

```
+-------------------------------------------------------+
|                 PRESENTATION LAYER                     |
|           REST API Controllers (Spring MVC)            |
|   TaskController    HealthController    Swagger UI     |
+---------------------------+---------------------------+
                            |
+---------------------------v---------------------------+
|                   BUSINESS LAYER                       |
|               Service Classes (Logic)                  |
|             TaskService (CRUD Operations)              |
+---------------------------+---------------------------+
                            |
+---------------------------v---------------------------+
|                     DATA LAYER                         |
|            JPA Repository (Spring Data)                |
|        H2 (Development)  /  MySQL (Production)         |
+-------------------------------------------------------+
```

---

## Tech Stack

| Category | Tools |
|---|---|
| **Language** | Java 17 |
| **Framework** | Spring Boot 3.2.4 |
| **Build Tool** | Apache Maven |
| **Database** | H2 (dev), MySQL 8.0 (prod) |
| **CI/CD** | GitHub Actions (7-stage pipeline) |
| **Unit Testing** | JUnit 5, Mockito |
| **Integration Testing** | Spring MockMvc |
| **API Testing** | Postman / Newman |
| **E2E Testing** | Selenium WebDriver |
| **Code Coverage** | JaCoCo |
| **Containerization** | Docker (multi-stage build) |
| **Orchestration** | Kubernetes, Minikube |
| **Monitoring** | Prometheus, Grafana |
| **Security** | SonarQube, Trivy, OWASP Dependency Check |
| **API Docs** | Springdoc OpenAPI (Swagger UI) |

---

## Project Structure

```
Automated-software-Deployment/
├── pom.xml                                     # Maven build config + dependencies
├── Dockerfile                                  # Multi-stage Docker build
├── docker-compose.yml                          # 5-service stack (app + DB + monitoring)
├── sonar-project.properties                    # SonarQube analysis config
├── .gitignore
│
├── src/main/java/com/deploymentpipeline/
│   ├── Application.java                        # Spring Boot entry point
│   ├── model/
│   │   ├── Task.java                           # Task entity (JPA)
│   │   ├── TaskStatus.java                     # Enum: PENDING, IN_PROGRESS, COMPLETED, CANCELLED
│   │   └── TaskPriority.java                   # Enum: LOW, MEDIUM, HIGH, CRITICAL
│   ├── repository/
│   │   └── TaskRepository.java                 # JPA data access layer
│   ├── service/
│   │   └── TaskService.java                    # Business logic layer
│   └── controller/
│       ├── TaskController.java                 # REST API endpoints
│       └── HealthController.java               # Health check endpoint
│
├── src/main/resources/
│   ├── application.properties                  # Dev config (H2 in-memory DB)
│   └── application-prod.properties             # Production config (MySQL)
│
├── src/test/java/com/deploymentpipeline/
│   ├── unit/
│   │   ├── TaskServiceTest.java                # 9 service layer unit tests
│   │   └── TaskControllerTest.java             # 6 controller layer unit tests
│   ├── integration/
│   │   └── TaskIntegrationTest.java            # 4 full API integration tests
│   └── e2e/
│       └── SeleniumE2ETest.java                # 3 browser-based E2E tests
│
├── tests/postman/
│   └── Task_API_Collection.json                # 7 Postman API test requests
│
├── k8s/
│   ├── namespace.yaml                          # Kubernetes namespace
│   ├── configmap.yaml                          # Application configuration
│   ├── secret.yaml                             # Sensitive data (Base64 encoded)
│   ├── mysql-deployment.yaml                   # MySQL pod + service + PVC
│   └── app-deployment.yaml                     # App (3 replicas) + HPA + LoadBalancer
│
├── monitoring/
│   ├── prometheus.yml                          # Prometheus scrape configuration
│   └── grafana/
│       └── dashboard.json                      # Pre-built 6-panel Grafana dashboard
│
├── .github/workflows/
│   └── ci-cd-pipeline.yml                      # 7-stage CI/CD pipeline
│
└── documentation.html                          # Complete HTML documentation with diagrams
```

---

## Application Features

The sample application is a **Task Manager REST API** that supports:

- Create, Read, Update, Delete (CRUD) operations for tasks
- Filter tasks by status (PENDING, IN_PROGRESS, COMPLETED, CANCELLED)
- Filter tasks by priority (LOW, MEDIUM, HIGH, CRITICAL)
- Search tasks by keyword
- Health check endpoint for monitoring
- Swagger UI for interactive API documentation
- Prometheus metrics endpoint for monitoring integration

---

## CI/CD Pipeline

The GitHub Actions pipeline runs automatically on every push to `main` or `develop` branches.

```
  ┌─────────┐
  │  BUILD  │
  └────┬────┘
       │
       ├──────────────────┬──────────────────┐
       v                  v                  v
  ┌──────────┐    ┌──────────────┐    ┌────────────┐
  │  UNIT    │    │ INTEGRATION  │    │  SECURITY  │    (Parallel)
  │  TESTS   │    │   TESTS      │    │   SCAN     │
  └────┬─────┘    └──────┬───────┘    └─────┬──────┘
       └────────────┬────┴──────────────────┘
                    v
            ┌──────────────┐
            │ DOCKER BUILD │
            │   & PUSH     │
            └──────┬───────┘
                   v
            ┌──────────────┐
            │   DEPLOY     │
            │   STAGING    │
            └──────┬───────┘
                   v
            ┌──────────────┐
            │   DEPLOY     │
            │  PRODUCTION  │
            └──────────────┘
```

### Pipeline Stages

| Stage | Tool | Description |
|---|---|---|
| 1. Build | Maven | Compile source code and package JAR |
| 2. Unit Tests | JUnit 5 | Run 15 unit tests on service and controller layers |
| 3. Integration Tests | Spring MockMvc | Run 4 full API lifecycle tests |
| 4. Security Scan | SonarQube + OWASP | Static code analysis and dependency vulnerability check |
| 5. Docker Build | Docker + Trivy | Build container image and scan for CVEs |
| 6. Deploy Staging | Kubernetes | Deploy to staging environment for verification |
| 7. Deploy Production | Kubernetes | Deploy to production after staging approval |

---

## Automated Testing

### Testing Pyramid

```
            /\
           /  \          E2E Tests (3) - Selenium
          / E2E\         Browser-based user simulation
         /------\
        /        \       Integration Tests (4) - Spring MockMvc
       / Integra- \      Full API flow with real database
      /   tion     \
     /--------------\
    /                \   Unit Tests (15) - JUnit 5 + Mockito
   /   Unit Tests     \  Individual method testing
  /____________________\

  Total: 22 automated tests
```

### Running Tests

```bash
# Run all tests
mvn test

# Run only unit tests
mvn test -Dtest="com.deploymentpipeline.unit.**"

# Run only integration tests
mvn test -Dtest="com.deploymentpipeline.integration.**"

# Run Postman tests via Newman
npm install -g newman
newman run tests/postman/Task_API_Collection.json

# Generate code coverage report
mvn test jacoco:report
# Report at: target/site/jacoco/index.html
```

---

## Containerization

### Docker

Multi-stage Dockerfile for optimized image size:

- **Stage 1 (Build):** Uses Maven to compile and package the JAR
- **Stage 2 (Run):** Uses lightweight Alpine JRE to run the application

```bash
# Build Docker image
docker build -t automated-deployment-system .

# Run container
docker run -p 8080:8080 automated-deployment-system
```

### Docker Compose

Runs 5 services together:

| Service | Port | Purpose |
|---|---|---|
| App | 8080 | Spring Boot REST API |
| MySQL | 3306 | Production database |
| Prometheus | 9090 | Metrics collection |
| Grafana | 3000 | Monitoring dashboards |
| SonarQube | 9000 | Code quality analysis |

```bash
# Start all services
docker-compose up -d

# Check status
docker-compose ps

# Stop all services
docker-compose down
```

---

## Kubernetes Orchestration

Kubernetes deployment with:

- **3 application replicas** with rolling update strategy
- **Horizontal Pod Autoscaler** (scales from 2 to 10 pods based on CPU usage)
- **Readiness and liveness probes** for health monitoring
- **ConfigMap and Secrets** for configuration management
- **Persistent Volume** for MySQL data storage
- **LoadBalancer service** for external access

```bash
# Start Minikube
minikube start

# Deploy all resources
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secret.yaml
kubectl apply -f k8s/mysql-deployment.yaml
kubectl apply -f k8s/app-deployment.yaml

# Check deployment
kubectl get all -n deployment-system

# Access application
minikube service app-service -n deployment-system
```

---

## Monitoring & Feedback

### Prometheus

Scrapes application metrics every 5 seconds from the `/actuator/prometheus` endpoint.

**Metrics collected:**
- HTTP request rate and response times
- JVM heap memory usage
- Active thread count
- System CPU usage
- Application uptime

### Grafana Dashboard

Pre-configured 6-panel dashboard:

| Panel | Metric | Description |
|---|---|---|
| HTTP Request Rate | `rate(http_server_requests_seconds_count[5m])` | Requests per second |
| Response Time (P95) | `histogram_quantile(0.95, ...)` | 95th percentile latency |
| JVM Memory Usage | `jvm_memory_used_bytes` | Heap memory consumption |
| Error Rate | `rate(...{status=~"5.."}[5m])` | 5xx errors per second |
| Active Threads | `jvm_threads_live_threads` | Live thread count |
| CPU Usage | `system_cpu_usage` | System CPU utilization |

**Setup:** Open Grafana at `http://localhost:3000` (admin/admin) and import `monitoring/grafana/dashboard.json`.

---

## Security (DevSecOps)

Security is integrated at every stage of the pipeline:

| Tool | Stage | What It Checks |
|---|---|---|
| **SonarQube** | Pre-build | Code bugs, security hotspots, code smells, duplications |
| **OWASP Dependency Check** | Build | Known vulnerabilities in project dependencies |
| **Trivy** | Post-Docker build | CVEs in Docker image OS packages and libraries |
| **GitHub Secret Scanning** | Repository | Prevents accidental commit of secrets/credentials |

```bash
# Run SonarQube analysis locally
docker-compose up -d sonarqube
mvn sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=admin -Dsonar.password=admin

# Scan Docker image with Trivy
docker run --rm aquasec/trivy image automated-deployment-system:latest
```

---

## Standards Incorporated

### ISO/IEC 27001 - Information Security Management

| Control | Implementation |
|---|---|
| A.12.6 - Vulnerability Management | Automated scanning with Trivy and OWASP |
| A.14.2 - Secure Development | Static code analysis via SonarQube in CI pipeline |
| A.12.4 - Logging and Monitoring | Real-time metrics with Prometheus and Grafana |
| A.9.4 - Access Control | Secrets management via K8s Secrets and GitHub Secrets |
| A.12.1 - Operational Procedures | Fully automated deployment procedures |
| A.14.2.8 - Security Testing | Multi-level automated testing (unit, integration, E2E) |

### IEEE 730 - Software Quality Assurance Plans

| Section | Implementation |
|---|---|
| Test Planning | Organized test hierarchy (unit > integration > E2E) |
| Test Case Design | Named test cases with `@DisplayName` annotations |
| Test Execution | Automated execution on every commit via GitHub Actions |
| Code Coverage | JaCoCo generates coverage reports |
| Quality Metrics | SonarQube quality gates |
| Configuration Management | Git version control + Infrastructure as Code |

---

## Getting Started

### Prerequisites

- Java JDK 17
- Apache Maven 3.9+
- Git
- Docker Desktop (for containerization)
- Minikube + kubectl (for Kubernetes)

### Option 1: Run Locally

```bash
# Clone the repository
git clone https://github.com/Aparnasaii/Automated-software-Deployment.git
cd Automated-software-Deployment

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

Access the application:
- API: `http://localhost:8080/api/tasks`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- H2 Console: `http://localhost:8080/h2-console`

### Option 2: Run with Docker Compose

```bash
docker-compose up -d
```

### Option 3: Deploy to Kubernetes

```bash
minikube start
kubectl apply -f k8s/
minikube service app-service -n deployment-system
```

---

## API Reference

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/tasks` | Get all tasks |
| `GET` | `/api/tasks/{id}` | Get task by ID |
| `POST` | `/api/tasks` | Create a new task |
| `PUT` | `/api/tasks/{id}` | Update an existing task |
| `DELETE` | `/api/tasks/{id}` | Delete a task |
| `GET` | `/api/tasks/status/{status}` | Filter tasks by status |
| `GET` | `/api/tasks/priority/{priority}` | Filter tasks by priority |
| `GET` | `/api/tasks/search?keyword=X` | Search tasks by keyword |
| `GET` | `/api/health` | Application health check |
| `GET` | `/actuator/prometheus` | Prometheus metrics |

### Example Request

```bash
# Create a task
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title": "Deploy v1.0", "description": "Deploy to production", "priority": "HIGH"}'
```

### Example Response

```json
{
  "id": 1,
  "title": "Deploy v1.0",
  "description": "Deploy to production",
  "status": "PENDING",
  "priority": "HIGH",
  "createdAt": "2026-05-26T10:30:00",
  "updatedAt": "2026-05-26T10:30:00"
}
```

---

## Documentation

Open `documentation.html` in any browser for the complete step-by-step guide with architecture diagrams, pipeline flow charts, and detailed explanations of every component.
