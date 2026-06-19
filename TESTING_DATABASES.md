# Multi-Database Testing Guide

This document explains how to test Clinvio Nucleus against different databases using Maven profiles.

## Quick Start

### SQLite (Default)
```bash
# Uses in-memory SQLite, no setup required
./mvnw clean test
# Or explicitly:
./mvnw clean test -Psqlite
```

### PostgreSQL
```bash
# Requires PostgreSQL running on localhost:5432
# Database: clinvio_test
# User: postgres
# Password: postgres

./mvnw clean test -Ppostgres
```

### MySQL
```bash
# Requires MySQL running on localhost:3306
# Database: clinvio_test
# User: root
# Password: root

./mvnw clean test -Pmysql
```

### SQL Server
```bash
# Requires SQL Server running on localhost:1433
# Database: clinvio_test
# User: sa
# Password: Password123!

./mvnw clean test -Psqlserver
```

### Oracle
```bash
# Requires Oracle XE running on localhost:1521
# Database: XE
# User: system
# Password: oracle

./mvnw clean test -Poracle
```

## Setting Up Databases Locally

### Using Docker Compose

Use the provided Docker Compose file to start all databases:

```bash
docker-compose -f docker-compose-all-databases.yml up -d
```

This starts:
- PostgreSQL on port 5432
- MySQL on port 3306
- SQL Server on port 1433
- Oracle XE on port 1521
- pgAdmin (PostgreSQL admin) on port 5050
- phpMyAdmin (MySQL admin) on port 8081

### Using Individual Docker Commands

#### PostgreSQL
```bash
docker run -d \
  --name postgres \
  -e POSTGRES_DB=clinvio_test \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres:16-alpine
```

#### MySQL
```bash
docker run -d \
  --name mysql \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=clinvio_test \
  -p 3306:3306 \
  mysql:8.3
```

#### SQL Server
```bash
docker run -d \
  --name sqlserver \
  -e ACCEPT_EULA=Y \
  -e SA_PASSWORD=Password123! \
  -p 1433:1433 \
  mcr.microsoft.com/mssql/server:2022-latest
```

#### Oracle XE
```bash
docker run -d \
  --name oracle \
  -e ORACLE_PASSWORD=oracle \
  -p 1521:1521 \
  gvenzl/oracle-xe:21-alpine
```

## Advanced Testing

### Using Environment Variables

Override datasource properties via environment variables:

```bash
./mvnw clean test -Ppostgres \
  -DSPRING_DATASOURCE_URL="jdbc:postgresql://custom-host:5432/custom_db" \
  -DSPRING_DATASOURCE_USERNAME="custom_user" \
  -DSPRING_DATASOURCE_PASSWORD="custom_pass"
```

### Full Build with Tests

```bash
./mvnw clean install -Ppostgres
```

### Running Tests Only (Skip Build)

```bash
./mvnw test -Pmysql
```

### Running Specific Test Class

```bash
./mvnw test -Ppostgres -Dtest=PersistencePropertiesTest
```

### Running Tests with Debug Output

```bash
./mvnw clean test -Ppostgres -X
```

## GitHub Actions CI/CD

The repository includes a comprehensive multi-database testing workflow (`.github/workflows/test-databases.yml`) that:

1. **Runs tests against all 5 databases in parallel**
   - SQLite (no external service)
   - PostgreSQL (Docker service)
   - MySQL (Docker service)
   - SQL Server (Docker service)
   - Oracle (Docker service)

2. **Uses GitHub's service containers**
   - Automatic setup and teardown
   - Health checks for database readiness
   - Proper environment variables

3. **Uploads test artifacts**
   - Test results for each database
   - Available for download and analysis

4. **Provides summary reporting**
   - Matrix view of all database test results

### Viewing CI/CD Results

- Main branch builds: https://github.com/jokerz5575/nucleus/actions/workflows/build.yml
- Multi-database tests: https://github.com/jokerz5575/nucleus/actions/workflows/test-databases.yml

## Continuous Integration Behavior

### On Push to Main or Develop
- **build.yml**: Runs tests against SQLite with Java 21 and 23
- **test-databases.yml**: Runs full multi-database test suite

### On Pull Requests
- **build.yml**: Runs tests against SQLite to verify compilation
- **test-databases.yml**: Verifies compatibility across all databases

## Troubleshooting

### Database Connection Errors

**Problem**: `Connection refused`

**Solution**: 
1. Verify database is running: `docker ps`
2. Check database logs: `docker logs <container_name>`
3. Verify connection settings match the profile

### TestContainers Issues

**Problem**: `Cannot connect to Docker daemon`

**Solution**:
1. Ensure Docker is installed: `docker --version`
2. Start Docker daemon if not running
3. Verify Docker socket permissions

### Property Override Not Working

**Problem**: Environment variable not being picked up

**Solution**:
1. Use Maven property format: `-DSPRING_DATASOURCE_URL=...`
2. Verify property name matches application.yml
3. Check profile definition in pom.xml

## Best Practices

1. **Default to SQLite for local development**
   - Fastest execution
   - No external dependencies
   - In-memory or file-based storage

2. **Test against target database before deployment**
   - Use specific profile: `-Ppostgres`, `-Pmysql`, etc.
   - Verify all features work on production database

3. **Use Docker Compose for full stack testing**
   - All databases available simultaneously
   - Management UIs (pgAdmin, phpMyAdmin)
   - Easy to reset: `docker-compose down && docker-compose up`

4. **Run CI/CD multi-database tests before releasing**
   - Ensures compatibility with all supported databases
   - Catches database-specific issues early

## Profile Configuration Details

| Profile | Database | JDBC URL | Default User | Default Password | Port |
|---------|----------|----------|---------------|------------------|------|
| sqlite | SQLite | jdbc:sqlite:memory | N/A | N/A | N/A |
| postgres | PostgreSQL | jdbc:postgresql://localhost:5432/clinvio_test | postgres | postgres | 5432 |
| mysql | MySQL | jdbc:mysql://localhost:3306/clinvio_test | root | root | 3306 |
| sqlserver | SQL Server | jdbc:sqlserver://localhost:1433;database=clinvio_test | sa | Password123! | 1433 |
| oracle | Oracle | jdbc:oracle:thin:@localhost:1521:XE | system | oracle | 1521 |

## See Also

- [DATABASE_CONFIGURATION.md](DATABASE_CONFIGURATION.md) - Database setup and configuration
- [docker-compose-all-databases.yml](docker-compose-all-databases.yml) - Docker Compose definition
- [.env.example](.env.example) - Environment variables template
