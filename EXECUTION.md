# Clinvio Nucleus — Build & Deploy Instructions

## Prerequisites
- Java 21+
- Maven 3.9+ (wrapper included)

## Build Commands

### Build All Community Modules
```bash
# Install parent POM
./mvnw install -N -DskipTests

# Build all community modules
./mvnw install -DskipTests
```

> Professional and Enterprise modules are available under commercial license. Contact https://clinvio.hu for details.

## Using in Another Project

Community modules are available from Maven Central. No additional repository configuration required.

### Add Dependencies
```xml
<dependencies>
    <!-- Starter (includes all community modules) -->
    <dependency>
        <groupId>hu.clinvio</groupId>
        <artifactId>clinvio-nucleus-spring-boot-starter</artifactId>
        <version>2.0.0</version>
    </dependency>

    <!-- Individual modules (optional) -->
    <dependency>
        <groupId>hu.clinvio</groupId>
        <artifactId>clinvio-nucleus-persistence</artifactId>
        <version>2.0.0</version>
    </dependency>
    <dependency>
        <groupId>hu.clinvio</groupId>
        <artifactId>clinvio-nucleus-business</artifactId>
        <version>2.0.0</version>
    </dependency>
    <dependency>
        <groupId>hu.clinvio</groupId>
        <artifactId>clinvio-nucleus-security</artifactId>
        <version>2.0.0</version>
    </dependency>
</dependencies>
```

> Professional/Enterprise modules require a commercial license and the private Gitea repository. See https://clinvio.hu for details.

### Configure Application
```properties
# application.properties
spring.datasource.url=jdbc:sqlite:./data/app.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.hibernate.ddl-auto=update

clinvio.persistence.encryption-key=${CLINVIO_ENCRYPTION_KEY}
clinvio.nucleus.security.jwt.secret=${JWT_SECRET}
```

## Module List (Community — 8)

| Module | Purpose |
|--------|---------|
| clinvio-nucleus-core | Component model, dialect, renderer |
| clinvio-nucleus-components | UI components |
| clinvio-nucleus-themes | CSS design system |
| clinvio-nucleus-htmx | HTMX integration |
| clinvio-nucleus-persistence | Entities, encryption, SQLite |
| clinvio-nucleus-business | Services, controllers, workflow |
| clinvio-nucleus-security | JWT authentication |
| clinvio-nucleus-spring-boot-starter | Auto-configuration |

> 26 additional modules available in Professional and Enterprise tiers.
