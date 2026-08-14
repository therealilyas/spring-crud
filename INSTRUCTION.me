<div align="center">

# 🧩 Order · Payment · User Microservices

### A hands-on Spring Boot microservices playground

*CRUD APIs · RestTemplate service-to-service calls · Docker · JUnit + Mockito*

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.12-brightgreen?style=for-the-badge&logo=springboot)
![Maven](https://img.shields.io/badge/Maven-3.9%2B-red?style=for-the-badge&logo=apachemaven)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-336791?style=for-the-badge&logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker)

</div>

---

## 📖 Table of Contents

1. [What Is This Project?](#-what-is-this-project)
2. [The Big Idea — Explained Simply](#-the-big-idea--explained-simply)
3. [Architecture](#-architecture)
4. [Tech Stack](#-tech-stack)
5. [Ports](#-ports)
6. [Getting Started](#-getting-started)
7. [🎓 The Full Lesson: How This Project Was Built](#-the-full-lesson-how-this-project-was-built)
   - [Lesson 1 — What is CRUD?](#lesson-1--what-is-crud)
   - [Lesson 2 — What is a Microservice?](#lesson-2--what-is-a-microservice)
   - [Lesson 3 — Layers of a Spring Boot App](#lesson-3--layers-of-a-spring-boot-app)
   - [Lesson 4 — Building the User Service (CRUD from scratch)](#lesson-4--building-the-user-service-crud-from-scratch)
   - [Lesson 5 — Talking Between Services with RestTemplate](#lesson-5--talking-between-services-with-resttemplate)
   - [Lesson 6 — Handling Errors Gracefully](#lesson-6--handling-errors-gracefully)
   - [Lesson 7 — Writing Unit Tests](#lesson-7--writing-unit-tests)
   - [Lesson 8 — Docker: Running Your Database Anywhere](#lesson-8--docker-running-your-database-anywhere)
8. [Testing the APIs](#-testing-the-apis)
9. [Project Structure](#-project-structure)
10. [Design Notes](#-design-notes)
11. [Stopping Everything](#-stopping-everything)

---

## 📌 What Is This Project?

This repository holds **three small Spring Boot applications** that work together like real-world services in a company:

| Service | Job |
|---|---|
| 👤 **User Service** | Keeps track of people (name, email) |
| 📦 **Order Service** | Keeps track of orders, and checks with User Service that the buyer is real |
| 💳 **Payment Service** | Keeps track of payments for those orders |

Each one is a separate, independently runnable application — its own port, own codebase folder, own tests — but they all share one PostgreSQL database for simplicity, and Order Service **calls** User Service over HTTP to do its job. That's what makes this a *microservices* project instead of just three unrelated apps.

---

## 🧠 The Big Idea — Explained Simply

Imagine three kids running three different lemonade stands on the same street.

- **Stand A (User Service)** keeps the notebook of everyone's name and address.
- **Stand B (Order Service)** takes orders for lemonade — but before writing one down, it *runs over to Stand A* and asks "hey, is this person actually in your notebook?"
- **Stand C (Payment Service)** just tracks who paid, how, and how much.

None of the stands share a cash register. They each have their own job, their own rules, and their own "notebook" of logic — but they talk to each other when they need information they don't own themselves. That's a microservice architecture in one sentence: **small, independent apps that do one job well and talk to each other over the network when needed.**

---

## 🏗 Architecture

```
                   +----------------+
                   |   PostgreSQL   |
                   |    storedb     |
                   +-------+--------+
                           |
       +-------------------+-------------------+
       |                   |                   |
+------v------+     +------v------+     +------v------+
| User Service|     |Payment Svc  |     | Order Svc   |
|    :8081    |     |    :8083    |     |    :8082    |
+-------------+     +-------------+     +------+------+
                                              |
                                              | RestTemplate
                                              +-------> User Service
```

Each service owns its own Java package, repository, service interface, implementation, controller, DTOs, and exception handling. In this learning project all three point at the same database for simplicity — in a real production system, each service would get its **own** database or schema so a bug in one can never corrupt another's data.

---

## 🛠 Tech Stack

- **Java 17**
- **Spring Boot 3.3.12**
- **Maven**
- **Spring Data JPA / Hibernate**
- **PostgreSQL**
- **REST APIs**
- **RestTemplate** (synchronous service-to-service calls)
- **DTOs** (Data Transfer Objects)
- **Service + ServiceImpl architecture**
- **Global exception handling**
- **Bean Validation**
- **Mockito + JUnit 5** unit tests
- **Docker Compose** for PostgreSQL

---

## 🔌 Ports

| Service | Port |
|---|---|
| User Service | `8081` |
| Order Service | `8082` |
| Payment Service | `8083` |
| PostgreSQL | `5432` |

---

## 🚀 Getting Started

### 1. Requirements

Install:
- Java 17
- Maven 3.9+
- Docker + Docker Compose

Check everything is installed:

```bash
java -version
mvn -version
docker --version
docker compose version
```

### 2. Start PostgreSQL

```bash
docker compose up -d
```

Check it's healthy:

```bash
docker compose ps
```

### 3. Build everything

From the project root:

```bash
mvn clean test
mvn clean package -DskipTests
```

The first command runs every unit test in every module. The second builds runnable JAR files without re-running tests (since you just ran them).

### 4. Run the services

Open three terminals:

```bash
# Terminal 1
java -jar user-service/target/user-service-1.0.0.jar

# Terminal 2
java -jar payment-service/target/payment-service-1.0.0.jar

# Terminal 3
java -jar order-service/target/order-service-1.0.0.jar
```

Or just run the three `main()` classes directly from IntelliJ / Eclipse.

---

## 🎓 The Full Lesson: How This Project Was Built

> Everything below explains **why** the code looks the way it does — written so that even if you've never touched Spring Boot before, you'll understand each piece before you see it.

### Lesson 1 — What is CRUD?

CRUD stands for the four basic things almost every app needs to do with data:

| Letter | Means | HTTP Verb | Example |
|---|---|---|---|
| **C** | Create | `POST` | Add a new user |
| **R** | Read | `GET` | Look up a user |
| **U** | Update | `PUT` | Change a user's email |
| **D** | Delete | `DELETE` | Remove a user |

That's it. Every "CRUD app" is just software that does these four things to some kind of data — users, orders, payments, blog posts, whatever. Once you understand CRUD, you understand 90% of backend web development.

### Lesson 2 — What is a Microservice?

Instead of building **one giant app** that handles users, orders, and payments all in the same codebase (called a "monolith"), we split it into **three small apps**, each responsible for exactly one thing:

- User Service only knows about users.
- Order Service only knows about orders — but it's allowed to *ask* User Service questions.
- Payment Service only knows about payments.

**Why bother?** Because each piece can be built, tested, deployed, and scaled completely on its own. If Order Service gets 10x more traffic tomorrow, you only need to scale *that* service — not the whole system.

### Lesson 3 — Layers of a Spring Boot App

Every service in this repo is built in layers, like a sandwich. Each layer has exactly one job:

```
Controller   →  receives the HTTP request (the "front door")
   ↓
Service      →  the interface — defines WHAT can be done
   ↓
ServiceImpl  →  the implementation — defines HOW it's done
   ↓
Repository   →  talks to the database (Spring Data JPA does this for you)
   ↓
Entity       →  the Java class that maps to a database table
```

Requests and responses never use the `Entity` directly — they use **DTOs** (Data Transfer Objects) instead. Think of a DTO as a "safe copy" of the data you're willing to show the outside world, so you never accidentally leak internal database fields.

### Lesson 4 — Building the User Service (CRUD from scratch)

Here's the shape of a real CRUD flow, using User Service as the example.

**The Entity** — this is the actual database table, described as a Java class:

```java
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    // getters and setters
}
```

**The Repository** — Spring Data JPA writes the SQL for you. You just declare an interface:

```java
public interface UserRepository extends JpaRepository<User, Long> {
    // CRUD methods (save, findById, findAll, deleteById...) already exist here for free
}
```

**The Service interface** — the contract, what this service promises to do:

```java
public interface UserService {
    UserResponse createUser(UserRequest request);
    UserResponse getUserById(Long id);
    List<UserResponse> getAllUsers();
    UserResponse updateUser(Long id, UserUpdateRequest request);
    void deleteUser(Long id);
}
```

**The ServiceImpl** — the actual logic:

```java
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse createUser(UserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        User saved = userRepository.save(user);
        return new UserResponse(saved.getId(), saved.getName(), saved.getEmail());
    }

    // getUserById, getAllUsers, updateUser, deleteUser follow the same pattern
}
```

**The Controller** — the "front door" that turns HTTP requests into method calls:

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
```

Follow that same five-layer pattern and you can build a CRUD API for *anything* — users, products, books, movies, it doesn't matter.

### Lesson 5 — Talking Between Services with RestTemplate

This is the part that makes it a *microservices* project instead of three separate toy apps.

When someone creates an order, Order Service needs to check: **"does this user actually exist?"** It doesn't have access to the `users` table's Java code — so instead, it makes an HTTP call to User Service, exactly like a browser or Postman would.

```java
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

```java
@Service
public class OrderServiceImpl implements OrderService {

    private final RestTemplate restTemplate;
    private static final String USER_SERVICE_URL = "http://localhost:8081/api/users/";

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        // Ask User Service: does this person exist?
        UserResponse user = restTemplate.getForObject(
            USER_SERVICE_URL + request.getUserId(),
            UserResponse.class
        );

        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + request.getUserId());
        }

        // user is real — safe to create the order now
        // ...save order logic here
    }
}
```

This is called **synchronous inter-service communication** — Order Service pauses, waits for User Service's answer, and only continues once it gets a response. It's the simplest way two services can cooperate, and the natural first stepping stone before learning message queues or async communication later.

### Lesson 6 — Handling Errors Gracefully

What happens if someone asks for user `#9999` and that user doesn't exist? Without help, Spring would throw an ugly stack trace at the client. Instead, we catch it in one central place:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(ResourceNotFoundException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
```

Now, no matter which controller throws a `ResourceNotFoundException`, the client always gets a clean `404` with a readable message — instead of a scary Java exception dump. One handler, every controller covered.

### Lesson 7 — Writing Unit Tests

A unit test checks one small piece of logic **without** needing a real database or a real second service running. We do that using Mockito, which creates "fake" versions of the repository and RestTemplate that behave however we tell them to.

```java
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldCreateUserSuccessfully() {
        User user = new User(1L, "Ilyas", "ilyas@example.com");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.createUser(
            new UserRequest("Ilyas", "ilyas@example.com")
        );

        assertEquals("Ilyas", response.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }
}
```

`@Mock` fakes the repository. `@InjectMocks` drops that fake into the real `UserServiceImpl`. `when(...).thenReturn(...)` tells the fake exactly what to say back. Run it a thousand times a day and it never touches a real database — that's what makes unit tests fast.

### Lesson 8 — Docker: Running Your Database Anywhere

Instead of installing PostgreSQL manually on your machine, `docker-compose.yml` describes exactly what database container to run, so anyone who clones this repo gets the *identical* setup with one command:

```yaml
services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: storedb
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
```

```bash
docker compose up -d
```

That one line downloads Postgres (if you don't have it), starts it in the background, and exposes it on port `5432` — no manual installer, no version mismatches between teammates' laptops.

---

## 🧪 Testing the APIs

### User CRUD

```bash
# Create
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Ilyas","email":"ilyas@example.com"}'

# Get all
curl http://localhost:8081/api/users

# Get one
curl http://localhost:8081/api/users/1

# Update
curl -X PUT http://localhost:8081/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Ilyas Sultanov","email":"ilyas.sultanov@example.com"}'

# Delete
curl -X DELETE http://localhost:8081/api/users/1
```

### Payment CRUD

```bash
# Create
curl -X POST http://localhost:8083/api/payments \
  -H "Content-Type: application/json" \
  -d '{"orderId":1001,"amount":150.50,"method":"CARD","status":"PENDING"}'

# Get all
curl http://localhost:8083/api/payments

# Update
curl -X PUT http://localhost:8083/api/payments/1 \
  -H "Content-Type: application/json" \
  -d '{"orderId":1001,"amount":150.50,"method":"CARD","status":"PAID"}'
```

### Order CRUD + RestTemplate

First create a user and note the returned ID. Order Service will use `RestTemplate` to confirm that user exists before saving the order:

```bash
# Create
curl -X POST http://localhost:8082/api/orders \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"productName":"Laptop","quantity":1,"totalAmount":1200.00,"status":"CREATED"}'

# Get all
curl http://localhost:8082/api/orders

# Get one
curl http://localhost:8082/api/orders/1

# Update
curl -X PUT http://localhost:8082/api/orders/1 \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"productName":"MacBook","quantity":1,"totalAmount":1500.00,"status":"CONFIRMED"}'

# Delete
curl -X DELETE http://localhost:8082/api/orders/1
```

### Run tests only

```bash
mvn test
```

All tests are unit tests using Mockito to mock repositories and `RestTemplate` — no live database or running services required.

---

## 📂 Project Structure

```
order-payment-user-microservices/
├── pom.xml
├── docker-compose.yml
├── README.md
│
├── user-service/
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/example/userservice/
│       │   ├── UserServiceApplication.java
│       │   ├── config/RestTemplateConfig.java
│       │   ├── controller/UserController.java
│       │   ├── dto/UserRequest.java
│       │   ├── dto/UserResponse.java
│       │   ├── dto/UserUpdateRequest.java
│       │   ├── entity/User.java
│       │   ├── exception/GlobalExceptionHandler.java
│       │   ├── exception/ResourceNotFoundException.java
│       │   ├── repository/UserRepository.java
│       │   ├── service/UserService.java
│       │   └── service/impl/UserServiceImpl.java
│       └── test/java/.../service/UserServiceImplTest.java
│
├── payment-service/
│   └── same layered architecture
│
└── order-service/
    └── same layered architecture
```

---

## 🧭 Design Notes

**Service interface / impl split** — every service is split into `service/UserService.java` (the contract) and `service/impl/UserServiceImpl.java` (the logic). This keeps the API separate from its implementation, which makes swapping logic or writing tests much easier.

**DTOs everywhere** — controllers never expose JPA entities directly. Requests and responses always pass through DTOs, so the database shape and the API shape are free to evolve independently.

**RestTemplate** — Order Service holds a `RestTemplate` bean and calls `GET http://localhost:8081/api/users/{id}` to demonstrate synchronous inter-service communication — the simplest form of microservice-to-microservice talk.

**Database** — Hibernate is set to `spring.jpa.hibernate.ddl-auto=update`, which is convenient while learning because it auto-creates tables. For a real production system, swap this for Flyway or Liquibase migrations and set `ddl-auto=validate` instead, so schema changes are explicit and reviewed.

---

## 🛑 Stopping Everything

Stop the running services with `Ctrl+C` in each terminal, then:

```bash
docker compose down
```

To also wipe the PostgreSQL data volume:

```bash
docker compose down -v
```

---

<div align="center">

Built as a hands-on lesson in CRUD, microservices, and inter-service communication with Spring Boot.

</div>
