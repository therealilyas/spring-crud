# Order / Payment / User Microservices

A small production-style learning project using:

- Java 17
- Spring Boot 3.3.12
- Maven
- Spring Data JPA / Hibernate
- PostgreSQL
- REST APIs
- RestTemplate
- DTOs
- Service + ServiceImpl architecture
- Global exception handling
- Bean Validation
- Mockito + JUnit 5 unit tests
- Docker Compose for PostgreSQL

## Architecture

```text
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

Each service owns its own Java package, repository, service interface, implementation,
controller, DTOs and exception handling.

For simplicity, all three services use the same PostgreSQL database in this learning
project. In a larger production system, give each service its own database/schema.

## Ports

| Service | Port |
|---|---:|
| User Service | 8081 |
| Order Service | 8082 |
| Payment Service | 8083 |
| PostgreSQL | 5432 |

## 1. Requirements

Install:

- Java 17
- Maven 3.9+
- Docker + Docker Compose

Check:

```bash
java -version
mvn -version
docker --version
docker compose version
```

## 2. Start PostgreSQL

```bash
docker compose up -d
```

Check:

```bash
docker compose ps
```

## 3. Build everything

From the project root:

```bash
mvn clean test
mvn clean package -DskipTests
```

The first command runs all unit tests. The second creates executable JAR files.

## 4. Run services

Open three terminals.

### Terminal 1

```bash
java -jar user-service/target/user-service-1.0.0.jar
```

### Terminal 2

```bash
java -jar payment-service/target/payment-service-1.0.0.jar
```

### Terminal 3

```bash
java -jar order-service/target/order-service-1.0.0.jar
```

Or run the three `main()` classes directly from IntelliJ/Eclipse.

## 5. Test User CRUD

Create:

```bash
curl -X POST http://localhost:8081/api/users   -H "Content-Type: application/json"   -d '{"name":"Ilyas","email":"ilyas@example.com"}'
```

Get all:

```bash
curl http://localhost:8081/api/users
```

Get one:

```bash
curl http://localhost:8081/api/users/1
```

Update:

```bash
curl -X PUT http://localhost:8081/api/users/1   -H "Content-Type: application/json"   -d '{"name":"Ilyas Sultanov","email":"ilyas.sultanov@example.com"}'
```

Delete:

```bash
curl -X DELETE http://localhost:8081/api/users/1
```

## 6. Test Payment CRUD

Create:

```bash
curl -X POST http://localhost:8083/api/payments   -H "Content-Type: application/json"   -d '{"orderId":1001,"amount":150.50,"method":"CARD","status":"PENDING"}'
```

Get all:

```bash
curl http://localhost:8083/api/payments
```

Update:

```bash
curl -X PUT http://localhost:8083/api/payments/1   -H "Content-Type: application/json"   -d '{"orderId":1001,"amount":150.50,"method":"CARD","status":"PAID"}'
```

## 7. Test Order CRUD + RestTemplate

First create a user and note the returned ID.

Create an order. The Order Service uses `RestTemplate` to call the User Service
and verifies that the user exists before saving the order:

```bash
curl -X POST http://localhost:8082/api/orders   -H "Content-Type: application/json"   -d '{"userId":1,"productName":"Laptop","quantity":1,"totalAmount":1200.00,"status":"CREATED"}'
```

Get all:

```bash
curl http://localhost:8082/api/orders
```

Get one:

```bash
curl http://localhost:8082/api/orders/1
```

Update:

```bash
curl -X PUT http://localhost:8082/api/orders/1   -H "Content-Type: application/json"   -d '{"userId":1,"productName":"MacBook","quantity":1,"totalAmount":1500.00,"status":"CONFIRMED"}'
```

Delete:

```bash
curl -X DELETE http://localhost:8082/api/orders/1
```

## 8. Run tests only

```bash
mvn test
```

Tests are unit tests and use Mockito to mock repositories and RestTemplate.

## Project structure

```text
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

## Design notes

### Service interface

Every service uses:

```text
service/
    UserService.java

service/impl/
    UserServiceImpl.java
```

This keeps the API/contract separate from the implementation and makes unit testing
and future implementation changes easier.

### DTOs

Controllers do not expose JPA entities directly. Requests and responses use DTOs.

### RestTemplate

The Order Service contains a `RestTemplate` bean and calls:

```text
GET http://localhost:8081/api/users/{id}
```

This demonstrates synchronous inter-service communication.

### Database

Hibernate uses:

```properties
spring.jpa.hibernate.ddl-auto=update
```

This is convenient for learning. For production, use Flyway/Liquibase and
`ddl-auto=validate`.

## Stop everything

Stop services with `Ctrl+C`, then:

```bash
docker compose down
```

To remove the PostgreSQL volume too:

```bash
docker compose down -v
```
