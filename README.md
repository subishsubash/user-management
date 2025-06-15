# 🧑‍💼 User Management System

A secure and scalable Spring Boot application that provides REST APIs for user registration, retrieval, deletion, and listing. Built with modern Java practices, strong validation, logging, and clean architecture principles.

---

## 🚀 Features

- Register a new user with encrypted password
- View user details by username
- Delete a user (Admin-only)
- List all users (Admin-only)
- Basic Auth with role-based authorization
- Structured logging with generic logger class
- Error, Exception cases are handled with GlobalExceptionHandler using @RestControllerAdvice and @ExceptionHandler
- DTO mapping done with MapStruct
- API Documentation using OpenAPI and Swagger.

---

## 🛠️ Tech Stack

| Layer            | Technology                |
|------------------|---------------------------|
| Backend          | Spring Boot               |
| ORM              | Spring Data JPA + Hibernate |
| Database         | PostgreSQL (or any JPA-compatible DB) |
| Security         | Spring Security (HTTP Basic Auth) |
| Mapping          | MapStruct (DTO <-> Entity) |
| Logging          | Log4j2 + Custom Logger     |
| Validation       | Jakarta Bean Validation    |
| Dependency Mgmt  | Maven                      |
| Documentation    | OpenAPI 3.0                |
| Annotations      | Lombok (to reduce boilerplate) |

---

## 🔗 API Endpoints

| Method | Endpoint                   | Access        | Description                      |
|--------|----------------------------|---------------|----------------------------------|
| POST   | `/v1/api/users/register`   | Public        | Register a new user              |
| GET    | `/v1/api/users?{username}` | Authenticated | Get user details by username     |
| GET    | `/v1/api/users`            | Admin only    | Get all registered users         |
| DELETE | `/v1/api/users/{username}` | Admin only    | Delete a user by username        |

---

## 📦 Project Structure

### Code Structure
````
Base Package: com.subash.user.management
├── controller          # UserController
├── config              # Swagger Config 
├── model               # Entity and DTO classes
├── repository          # UserRepository interface
├── service             # Service interface and implementation
├── security            # Spring Security configurations
├── util                # Constants, Logging, and Exception Handler
├── mapper              # MapStruct mapper interface
└── UserManagementApplication.java
````

### Important Classes

- User: Entity representing the users table 
- UserController: Rest API Definitions
- UserRepository: Interface extending JpaRepository 
- UserService / Impl: Business logic for CRUD operations 
- CustomUserDetailsService: Loads user for authentication 
- SecurityConfig: HTTP security rules and authentication configuration 
- GenericLogger: Logs request and response bodies conditionally 
- GlobalExceptionHandler: Handles validation and general exceptions globally

---

## 🔐 Security

- Uses **HTTP Basic Authentication**
- Passwords encrypted via `BCryptPasswordEncoder`
- Role-based access:
    - `USER`: Can access only their data
    - `ADMIN`: Can access all users and delete users

---

## 📝 Logging

The `GenericLogger` utility class provides structured logging:

- Logs UUID, operation ID, status, request/response bodies
- Configurable using:
  ```properties
  print.log.enable.request=true #enable/disable logging of request
  print.log.enable.response=true #enable/disable logging of response

Sample Log Entry

````
2025-06-15T05:22:52.375+05:30  INFO 27868 --- [user-management] [nio-8080-exec-4] c.s.u.m.controller.UserController        : [UUID] : ae708d67-bd89-4b34-a818-be5864c4b626, [MESSAGE] : Request received to user registration
2025-06-15T05:22:52.400+05:30  INFO 27868 --- [user-management] [nio-8080-exec-4] c.s.u.m.controller.UserController        : [UUID] : ae708d67-bd89-4b34-a818-be5864c4b626, [APPLICATION] : User Management, [OPERATION ID] : createUser, [HTTP METHOD] : POST, [REQUEST BODY] : {"username":"subash12396_User1","password":"subash311211","role":"ADMIN","emailId":"subash12396_User1@gmail.com","phoneNumber":"9566773603"}
2025-06-15T05:22:52.400+05:30  INFO 27868 --- [user-management] [nio-8080-exec-4] c.s.u.m.service.UserServiceImpl          : [UUID] : ae708d67-bd89-4b34-a818-be5864c4b626, [MESSAGE] : Processing create user request
2025-06-15T05:22:52.832+05:30  INFO 27868 --- [user-management] [nio-8080-exec-4] c.s.u.m.service.UserServiceImpl          : [UUID] : ae708d67-bd89-4b34-a818-be5864c4b626, [MESSAGE] : Create user request processed
2025-06-15T05:22:52.862+05:30  INFO 27868 --- [user-management] [nio-8080-exec-4] c.s.u.m.controller.UserController        : [UUID] : ae708d67-bd89-4b34-a818-be5864c4b626, [APPLICATION] : User Management, [STATUS] : OK, [RESPONSE BODY] : {"headers":{},"body":{"user":{"username":"subash12396_User1","password":null,"role":"ADMIN","emailId":"subash12396_User1@gmail.com","phoneNumber":"9566773603"},"code":5001,"message":"User created successfully"},"statusCodeValue":201,"statusCode":"CREATED"}
2025-06-15T05:22:52.862+05:30  INFO 27868 --- [user-management] [nio-8080-exec-4] c.s.u.m.controller.UserController        : [UUID] : ae708d67-bd89-4b34-a818-be5864c4b626, [MESSAGE] : User registration request completed
````

## 🚨 Exception Handling

Handled using `@RestControllerAdvice` and `@ExceptionHandler` which helps to returns appropriate HttpStatus and JSON error messages.

| Exception Type                   | Response                            |
|----------------------------------|-------------------------------------|
| MethodArgumentNotValidException  | 400 Bad Request with field errors   |   
| HttpMessageNotReadableException  | 400 Bad Request for malformed JSON  |  
| Exception                        | 500 Internal Server Error           |    

## 🚀 Run Locally
### ✅ Prerequisites
 - Java 21+
 - Maven 3.8+

### 🏃‍Start the App

````
mvn clean install
mvn spring-boot:run
````

## 🔎 API Docs (Swagger UI)
Visit: http://localhost:8080/swagger-ui.html or /swagger-ui/index.html

## 📌 Future Enhancements 
- Switch to JWT-based authentication 
- Dockerize the application 
- Add integration test coverage 
- API rate limiting for production use
