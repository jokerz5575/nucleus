# Clinvio Nucleus — Technical Specification

**Version:** 2.0.0
**Date:** 2026-06-18
**Status:** Community Edition (MIT)

---

## 1. Executive Summary

Clinvio Nucleus is a modular enterprise Java framework for building secure, server-rendered web applications. It provides field-level AES-256-GCM encryption, HTMX partial updates, a component-based UI system, and workflow engine. This specification covers the **Community Edition** (MIT licensed). Additional Professional and Enterprise modules are available under commercial license.

---

## 2. Architecture

### 2.1 Module Dependency Graph

```
clinvio-nucleus-core
├── clinvio-nucleus-components
├── clinvio-nucleus-themes
├── clinvio-nucleus-htmx
├── clinvio-nucleus-persistence
└── clinvio-nucleus-business
    └── clinvio-nucleus-security
        └── clinvio-nucleus-spring-boot-starter
            └── Application
```

> Additional modules (observability, migration, cache, OpenAPI, WebSocket, scheduler, mail, compliance, appointment, documents, tasks, AI, multitenancy, notifications, PDF, S3, Redis, rate limiting, PostgreSQL, Docker, devtools, test, CLI, Maven plugin) are available in Professional and Enterprise tiers.

### 2.2 Request Flow

```
Browser → HxRequestFilter → DispatcherServlet → Controller
  → Service → Repository → EncryptedStringConverter → SQLite
  ← Thymeleaf + ClinvioDialect → HTML (or HxResponse)
  ← Browser
```

---

## 3. Security

### 3.1 Encryption

- **Algorithm:** AES-256-GCM
- **Key Derivation:** PBKDF2 with 600,000 iterations
- **IV:** 12 bytes random per encryption
- **Auth Tag:** 128-bit GCM tag

### 3.2 Authentication

- **Method:** JWT (JSON Web Tokens)
- **Signing:** HMAC-SHA256
- **Expiration:** Configurable (default 24h)
- **Access Control:** `@Secured(roles = {"ADMIN"})`

---

## 4. Configuration

```properties
# Database
spring.datasource.url=jdbc:sqlite:./data/app.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.hibernate.ddl-auto=update

# Encryption
clinvio.persistence.encryption-key=${CLINVIO_ENCRYPTION_KEY}

# Security
clinvio.nucleus.security.jwt.secret=${JWT_SECRET}
clinvio.nucleus.security.jwt.expiration=86400000

# (Professional/Enterprise modules add observability, cache, OpenAPI,
# scheduler, mail, compliance, multi-tenancy, and more — see commercial docs)
```

---

## 5. API Reference

### 5.1 REST Endpoints (Auto-generated)

| Method | Path | Description |
|--------|------|-------------|
| GET | /api/{entity} | List with pagination |
| GET | /api/{entity}/{id} | Get by ID |
| POST | /api/{entity} | Create |
| PUT | /api/{entity}/{id} | Update |
| DELETE | /api/{entity}/{id} | Soft delete |
| GET | /api/{entity}/all | Get all |
| GET | /api/{entity}/count | Count |

### 5.2 Health Endpoints

| Endpoint | Description |
|----------|-------------|
| /health | Aggregate health |
| /health/db | Database health |
| /health/encryption | Encryption health |
| /actuator/prometheus | Prometheus metrics |

---

## 6. Testing

```java
// Create test entities
Order order = TestDataFactory.createEntity(Order.class);
List<Order> orders = TestDataFactory.createEntities(Order.class, 10);
Page<Order> page = TestDataFactory.createPage(orders, 0, 10);

// Random data
String email = TestDataFactory.randomEmail();
int num = TestDataFactory.randomInt(1, 100);
```

---

## 7. Deployment

### 7.1 Package Registries

- **Community modules** (MIT): Published to [GitHub Packages](https://github.com/jokerz5575/nucleus/packages) and Maven Central
- **Professional/Enterprise modules** (commercial): Published to `https://dev.clinvio.hu/api/packages/jokerz/maven`

### 7.2 Maven Settings

```xml
<settings>
    <servers>
        <server>
            <id>gitea</id>
            <username>jokerz</username>
            <password><your-password></password>
        </server>
    </servers>
</settings>
```
