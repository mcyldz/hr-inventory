# HR Inventory Management System API

## 📝 Project Description

**HR Inventory Management System API** is a backend application developed for managing a company’s Human Resources (HR) and inventory processes.

This API provides the following core functionalities:

- Personnel Management
- Inventory Tracking
- Asset Assignment and Return

The project is built with best practices and design patterns from the Java and Spring Boot ecosystem, structured to follow enterprise-level software architecture.

---

## 🚀 Tech Stack & Tools

- **Backend:** Java 21, Spring Boot 3.x  
- **Data Access:** Spring Data JPA, Hibernate  
- **Database:** PostgreSQL  
- **Security:** Spring Security, JWT (JSON Web Token)  
- **API & Documentation:** RESTful API, Springdoc-openapi (Swagger UI)  
- **Object Mapping:** MapStruct  
- **Utilities:** Lombok  
- **Build Tool:** Apache Maven  
- **Database Management:** DBeaver  
- **API Testing Tools:** Postman, Swagger UI

---

## 🧱 Architecture & Design Principles

### Layered Architecture

The application is structured in clearly separated layers:

- **Controller:** Handles HTTP requests and routing.
- **Service:** Contains business logic.
- **Repository:** Manages database operations.
- **Entity:** Represents database tables.
- **DTO (Data Transfer Object):** Defines data structures for requests/responses.
- **Mapper:** Converts between Entities and DTOs.

### DTO Pattern

- Ensures sensitive data is hidden from API consumers.
- Decouples API contracts from the database schema.
- Uses structures such as `Request` and `Response`.

### Centralized Exception Handling

- Implemented with `@RestControllerAdvice` and a `GlobalExceptionHandler`.
- Returns consistent error responses using a standardized `ErrorResponse` DTO and `ErrorCode` enum.

### Programming to an Interface

- Service layer is abstracted with interfaces.
- Enhances testability, modularity, and scalability.

---

## ⚙️ Getting Started

### Prerequisites

- Java Development Kit (JDK) 21
- Apache Maven
- PostgreSQL

### Clone the Repository

```bash
git clone 
cd hr-inventory
```

### Database Setup

Create a PostgreSQL database named hr_inventory_db:

```bash
CREATE DATABASE hr_inventory_db;
```

### Configuration

Update the database credentials in src/main/resources/application-dev.yml:

```bash
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/hr_inventory_db
    username: ${DB_USER}
    password: ${DB_PASSWORD}
    driver-class-name: org.postgresql.Driver
```

### Run the Application

Using an IDE (e.g., IntelliJ IDEA):

Open the project.

Run HrInventoryManagementApplication.java.

Via Command Line:

```bash
mvn spring-boot:run
```

# 📚 API Reference

ACCESS INTERACTIVE API DOCUMENTATION AT:

http://localhost:8080/swagger-ui.html

# 🔐 Security

The API is secured using JWT (JSON Web Token) authentication.

After successful login, the token must be provided in the request header:

Authorization: Bearer <jwt-token>

Role-based access control is enforced using @PreAuthorize.

# 📫 Contact

For questions or contributions, please open an issue or contact the developer via the GitHub repository.