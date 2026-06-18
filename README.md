# Clinvio Nucleus

[![Build](https://github.com/jokerz5575/nucleus/actions/workflows/build.yml/badge.svg)](https://github.com/jokerz5575/nucleus/actions/workflows/build.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-21%2B-blue)](https://adoptium.net)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen)](https://spring.io/projects/spring-boot)
[![HTMX](https://img.shields.io/badge/HTMX-2.0.4-blueviolet)](https://htmx.org)
[![Tests](https://img.shields.io/badge/tests-160%2B-passing-green)](https://github.com/jokerz5575/nucleus/actions)

Enterprise Java framework for building secure, server-rendered web applications with field-level AES-256-GCM encryption, HTMX partial updates, and modular architecture.

**This is the Community edition (MIT).** Full Professional and Enterprise editions with additional modules are available at [clinvio.hu/nucleus](https://clinvio.hu/nucleus).

---

## Quick Start

Add the dependency:

```xml
<dependency>
    <groupId>hu.clinvio</groupId>
    <artifactId>clinvio-nucleus-spring-boot-starter</artifactId>
    <version>2.3.0</version>
</dependency>
```

```java
@SpringBootApplication
public class App { public static void main(String[] args) { SpringApplication.run(App.class, args); } }
```

```properties
spring.datasource.url=jdbc:sqlite:./data/app.db
clinvio.persistence.encryption-key=${CLINVIO_ENCRYPTION_KEY}
```

## Features

- **Field-level encryption** — `@SensitiveData` + `@Convert` on any JPA field, AES-256-GCM
- **HTMX-native UI** — ~14KB client, no JavaScript build step
- **8 modular jars** (Community) — core, components, themes, HTMX, persistence, business, security, starter
- **SQLite production-ready** — zero-config, switch to PostgreSQL with Professional tier
- **JWT auth** — Spring Security integration with refresh, logout, and CORS
- **23 UI components** — buttons, forms, data tables, cards, toasts, and more
- **MIT licensed** — free to use, modify, and distribute

## Community Modules (Free, MIT)

| Module | Purpose |
|--------|---------|
| `clinvio-nucleus-core` | Component model, dialect, renderer |
| `clinvio-nucleus-components` | UI components (data table, cards, forms) |
| `clinvio-nucleus-themes` | CSS design system |
| `clinvio-nucleus-htmx` | HTMX request/response handling |
| `clinvio-nucleus-persistence` | Entities, AES-256-GCM encryption, SQLite dialect |
| `clinvio-nucleus-business` | Services, controllers, workflow engine |
| `clinvio-nucleus-security` | JWT authentication, role-based access control |
| `clinvio-nucleus-spring-boot-starter` | Auto-configuration |

## Professional & Enterprise Modules (Paid)

Additional modules available with paid tiers at [clinvio.hu/nucleus](https://clinvio.hu/nucleus):

| Category | Modules |
|----------|---------|
| **Infrastructure** | PostgreSQL, Redis, S3 storage, Elasticsearch, observability, rate limiting |
| **DevTools** | Docker support, PDF generation, migration, CLI, Maven plugin, test utilities |
| **Domain** | AI/LLM integration, compliance/GDPR, appointments, documents, tasks, notifications, multi-tenancy |
| **Real-time** | WebSocket, scheduler, mail, OpenAPI |

## Encryption

AES-256-GCM per NIST SP 800-38D, PBKDF2 with 600K iterations per OWASP 2023:

```java
@Entity
public class Patient {
    @Id private Long id;

    @SensitiveData
    @Convert(converter = EncryptedStringConverter.class)
    private String ssn;
}
```

## Build

```bash
./mvnw install -N -DskipTests      # Install parent POM
./mvnw install -DskipTests          # Build all Community modules
./mvnw test                         # Run tests
```

## Documentation

- [Changelog](CHANGELOG.md)
- [Roadmap](ROADMAP.md)
- [Contributing](CONTRIBUTING.md)
- [Security](SECURITY.md)
- [Action Plan](ACTION-PLAN.md)

## License

Community edition: MIT — see [LICENSE](LICENSE).

Professional and Enterprise editions: proprietary — see [clinvio.hu/nucleus](https://clinvio.hu/nucleus).

## Links

- [Website](https://clinvio.hu/nucleus)
- [Pricing](https://clinvio.hu/nucleus/pricing)
- [GitHub](https://github.com/jokerz5575/nucleus)
- [Issue Tracker](https://github.com/jokerz5575/nucleus/issues)
