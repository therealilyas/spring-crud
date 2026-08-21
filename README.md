<div align="center">

# 🧩 Spring Microservices: Order-Payment-User

### *A Production-Ready Template for Synchronous Microservices Communication*

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.12-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)](LICENSE)

</div>

---

## 📋 Overview

A **production-grade microservices architecture** demonstrating synchronous inter-service communication using **RestTemplate**. Built with Spring Boot 3.3.12, this project implements a complete Order-Payment-User system with:

- ✅ Service discovery and communication
- ✅ Database persistence with PostgreSQL
- ✅ Containerization with Docker Compose
- ✅ Comprehensive unit testing
- ✅ Global exception handling
- ✅ DTO-based data transfer

---

## 🎯 System Architecture

### High-Level Overview

```
┌─────────────────────────────────────────────────────────────────────┐
│                        MICROSERVICES ARCHITECTURE                   │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│                         ┌──────────────────┐                        │
│                         │   PostgreSQL     │                        │
│                         │   Database       │                        │
│                         │   (Port: 5432)   │                        │
│                         └────────┬─────────┘                        │
│                                  │                                  │
│              ┌───────────────────┼───────────────────┐              │
│              │                   │                   │              │
│              ▼                   ▼                   ▼              │
│   ┌──────────────────┐ ┌──────────────────┐ ┌──────────────────┐  │
│   │   User Service   │ │  Payment Service │ │   Order Service  │  │
│   │   (Port: 8081)   │ │   (Port: 8083)   │ │   (Port: 8082)   │  │
│   │                  │ │                  │ │                  │  │
│   │ • CRUD Users     │ │ • CRUD Payments  │ │ • CRUD Orders    │  │
│   │ • User Validation│ │ • User Validation│ │ • Auto-Payments  │  │
│   │                  │ │ • Order Valida-  │ │ • User Valida-   │  │
│   │                  │ │   tion           │ │   tion           │  │
│   └────────┬─────────┘ └────────┬─────────┘ └────────┬─────────┘  │
│            │                    │                    │             │
│            └────────────────────┼────────────────────┘             │
│                                  │                                  │
│                    ┌─────────────▼─────────────┐                   │
│                    │   REST API Communication   │                   │
│                    │      (Synchronous)         │                   │
│                    │      (RestTemplate)        │                   │
│                    └───────────────────────────┘                   │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

### Communication Matrix

```
┌─────────────────────────────────────────────────────────────────────┐
│                      SERVICE COMMUNICATION MATRIX                   │
├──────────┬──────────┬──────────┬─────────────────────────────────┤
│  Source  │  Target  │   Type   │           Description           │
├──────────┼──────────┼──────────┼─────────────────────────────────┤
│  Order   │  User    │  GET     │ Validate user exists             │
│  Order   │  Payment │  POST    │ Auto-create payment for order    │
│  Payment │  User    │  GET     │ Validate user exists             │
│  Payment │  Order   │  GET     │ Validate order exists            │
└──────────┴──────────┴──────────┴─────────────────────────────────┘
```

### Synchronous Communication Flow

```
Client           Order Service          User Service         Payment Service
  │                   │                      │                     │
  │  1. POST /orders  │                      │                     │
  │──────────────────►│                      │                     │
  │                   │  2. GET /users/{id}  │                     │
  │                   │─────────────────────►│                     │
  │                   │  3. 200 OK (User)    │                     │
  │                   │◄─────────────────────│                     │
  │                   │                      │                     │
  │                   │  4. Save Order       │                     │
  │                   │  (Status: CREATED)   │                     │
  │                   │                      │                     │
  │                   │  5. POST /payments   │                     │
  │                   │───────────────────────────────────────────►│
  │                   │                      │                     │
  │                   │                      │  6. GET /users/{id} │
  │                   │                      │◄────────────────────│
  │                   │                      │  7. 200 OK         │
  │                   │                      │────────────────────►│
  │                   │                      │                     │
  │                   │                      │  8. GET /orders/{id}│
  │                   │◄───────────────────────────────────────────│
  │                   │                      │  9. 200 OK         │
  │                   │───────────────────────────────────────────►│
  │                   │                      │                     │
  │                   │  10. 201 Created     │                     │
  │                   │◄───────────────────────────────────────────│
  │  11. 201 Created  │                      │                     │
  │◄──────────────────│                      │                     │
  │                   │                      │                     │
  └─────────────────────────────────────────────────────────────────┘
```

---

## 🔧 Technical Specifications

### Technology Stack

| Component | Technology | Version |
|-----------|------------|---------|
| Language | Java | 17 |
| Framework | Spring Boot | 3.3.12 |
| Build Tool | Maven | 3.9+ |
| ORM | Spring Data JPA / Hibernate | - |
| Database | PostgreSQL | 15 |
| Communication | RestTemplate | Synchronous |
| Containerization | Docker Compose | Latest |
| Testing | JUnit 5 + Mockito | - |
| Validation | Bean Validation (Hibernate) | - |

### Service Ports

| Service | Port | Description |
|---------|------|-------------|
| User Service | 8081 | User management and validation |
| Order Service | 8082 | Order management with auto-payments |
| Payment Service | 8083 | Payment management with validation |
| PostgreSQL | 5432 | Database server |

---

## 🚀 Deployment Guide

### Prerequisites

```bash
# Required installations
- Java 17+
- Maven 3.9+
- Docker 20.10+
- Docker Compose 2.0+
```

### Local Development Setup

1. **Clone the Repository**
```bash
git clone https://github.com/therealilyas/spring-crud.git
cd spring-crud
```

2. **Start Database Container**
```bash
docker compose up -d
```

3. **Build Services**
```bash
mvn clean package -DskipTests
```

4. **Run Services**
```bash
# Terminal 1
java -jar user-service/target/user-service-1.0.0.jar

# Terminal 2
java -jar payment-service/target/payment-service-1.0.0.jar

# Terminal 3
java -jar order-service/target/order-service-1.0.0.jar
```

### Using IDE

Run each service's `Application.java` class directly:
- `UserServiceApplication` → port 8081
- `PaymentServiceApplication` → port 8083
- `OrderServiceApplication` → port 8082

---

## 📡 API Reference

### User Service (`:8081`)

```
GET    /api/users          → List all users
GET    /api/users/{id}     → Get user by ID
POST   /api/users          → Create new user
PUT    /api/users/{id}     → Update user
DELETE /api/users/{id}     → Delete user
```

**POST Example:**
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com"
}
```

### Order Service (`:8082`)

```
GET    /api/orders          → List all orders
GET    /api/orders/{id}     → Get order by ID
POST   /api/orders          → Create order (auto-creates payment)
PUT    /api/orders/{id}     → Update order
DELETE /api/orders/{id}     → Delete order
```

**POST Example:**
```json
{
  "userId": 1,
  "productName": "MacBook Pro",
  "quantity": 1,
  "totalAmount": 2499.99,
  "status": "CREATED"
}
```

### Payment Service (`:8083`)

```
GET    /api/payments          → List all payments
GET    /api/payments/{id}     → Get payment by ID
POST   /api/payments          → Create payment (validates user & order)
PUT    /api/payments/{id}     → Update payment
DELETE /api/payments/{id}     → Delete payment
```

**POST Example:**
```json
{
  "userId": 1,
  "orderId": 1,
  "amount": 2499.99,
  "method": "CARD",
  "status": "PAID"
}
```

---

## 🧪 Testing

### Run Unit Tests
```bash
mvn test
```

### Sample API Tests

```bash
# Create User
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Ilyas","email":"ilyas@example.com"}'

# Create Order (auto-creates payment)
curl -X POST http://localhost:8082/api/orders \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"productName":"Laptop","quantity":1,"totalAmount":1200.00,"status":"CREATED"}'

# Create Payment (validates user & order)
curl -X POST http://localhost:8083/api/payments \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"orderId":1,"amount":1200.00,"method":"CARD","status":"PAID"}'
```

---

## 📁 Project Structure

```
spring-crud/
├── 📄 pom.xml                      # Parent POM
├── 📄 docker-compose.yml           # PostgreSQL container
├── 📄 README.md
│
├── 📁 user-service/                # User Service (port 8081)
│   ├── 📄 pom.xml
│   └── 📁 src/main/java/com/example/userservice/
│       ├── 📁 config/RestTemplateConfig.java
│       ├── 📁 controller/UserController.java
│       ├── 📁 dto/
│       ├── 📁 entity/User.java
│       ├── 📁 exception/
│       ├── 📁 repository/UserRepository.java
│       └── 📁 service/
│
├── 📁 order-service/               # Order Service (port 8082)
│   ├── 📄 pom.xml
│   └── 📁 src/main/java/com/example/orderservice/
│       ├── 📁 client/UserClient.java
│       ├── 📁 config/RestTemplateConfig.java
│       ├── 📁 controller/OrderController.java
│       ├── 📁 dto/
│       ├── 📁 entity/Order.java
│       ├── 📁 exception/
│       ├── 📁 repository/OrderRepository.java
│       └── 📁 service/
│
└── 📁 payment-service/             # Payment Service (port 8083)
    ├── 📄 pom.xml
    └── 📁 src/main/java/com/example/paymentservice/
        ├── 📁 config/RestTemplateConfig.java
        ├── 📁 controller/PaymentController.java
        ├── 📁 dto/
        ├── 📁 entity/Payment.java
        ├── 📁 exception/
        ├── 📁 repository/PaymentRepository.java
        └── 📁 service/
```

---

## 🎯 Key Features

- ✅ **Service Discovery** - All services discover each other via HTTP
- ✅ **Synchronous Communication** - Real-time request-response pattern
- ✅ **Data Validation** - Multi-service validation for data integrity
- ✅ **Auto-creation** - Payments auto-created during order creation
- ✅ **Global Exception Handling** - Unified error responses
- ✅ **DTO Pattern** - Secure data transfer between services
- ✅ **Containerization** - Docker Compose for easy deployment
- ✅ **Testing** - Comprehensive unit tests with JUnit & Mockito

---

## 🔒 Security & Best Practices

- **DTO-based communication**: No direct entity exposure
- **Validation**: Bean validation for request data
- **Error Handling**: Global exception handling with meaningful messages
- **Logging**: Comprehensive logging for debugging
- **Stateless Design**: Services are stateless, scalable

---

## 📊 Performance Considerations

| Aspect | Implementation |
|--------|---------------|
| Communication | Synchronous (RestTemplate) |
| Database Connection | Connection pooling (HikariCP) |
| Thread Model | Per-request thread (Spring MVC) |
| Response Time | ~200-500ms (typical) |
| Scalability | Horizontal scaling per service |

---

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.

---

## 📧 Contact

**Ilyas Sultanov** - [@therealilyas](https://github.com/therealilyas)

Project Link: [https://github.com/therealilyas/spring-crud](https://github.com/therealilyas/spring-crud)

---

<div align="center">

### ⭐ Star this repository to support the project!

[![GitHub stars](https://img.shields.io/github/stars/therealilyas/spring-crud?style=social)](https://github.com/therealilyas/spring-crud/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/therealilyas/spring-crud?style=social)](https://github.com/therealilyas/spring-crud/network/members)
[![GitHub watchers](https://img.shields.io/github/watchers/therealilyas/spring-crud?style=social)](https://github.com/therealilyas/spring-crud/watchers)

</div>

---

*Built as a comprehensive learning resource for Spring Boot microservices with synchronous communication.*