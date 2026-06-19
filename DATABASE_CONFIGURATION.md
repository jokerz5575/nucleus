# Clinvio Nucleus - Multi-Database Configuration Guide

This guide explains how to configure Clinvio Nucleus with different database systems. As of v2.5.0, Clinvio Nucleus supports five database systems out of the box.

## Supported Databases

### ✅ SQLite (Default)
- **Best for:** Development, testing, single-file deployments
- **Driver:** org.xerial:sqlite-jdbc:3.53.2.0
- **Hibernate Dialect:** org.hibernate.community.dialect.SQLiteDialect

### ✅ PostgreSQL
- **Best for:** Enterprise, production, cloud deployments
- **Driver:** org.postgresql:postgresql:42.7.3
- **Hibernate Dialect:** org.hibernate.dialect.PostgreSQLDialect

### ✅ MySQL / MariaDB
- **Best for:** Web applications, shared hosting, multi-user systems
- **Driver:** com.mysql:mysql-connector-j:8.3.0
- **Hibernate Dialect:** org.hibernate.dialect.MySQL8Dialect

### ✅ Oracle Database
- **Best for:** Enterprise systems, legacy migrations
- **Driver:** com.oracle.database.jdbc:ojdbc11:23.3.0.23.09
- **Hibernate Dialect:** org.hibernate.dialect.OracleDialect

### ✅ SQL Server
- **Best for:** Microsoft shops, Azure deployments
- **Driver:** com.microsoft.sqlserver:mssql-jdbc:12.4.2.jre11
- **Hibernate Dialect:** org.hibernate.dialect.SQLServerDialect

---

## Quick Start Examples

### SQLite (Default - No Configuration Required)

```properties
# application.properties
clinvio.persistence.encryption-key=my-secret-key
clinvio.persistence.sqlite.database-path=./data/clinvio.db
```

### PostgreSQL

```properties
# application.properties
clinvio.persistence.type=postgresql
clinvio.persistence.encryption-key=my-secret-key
clinvio.persistence.postgresql.host=localhost
clinvio.persistence.postgresql.port=5432
clinvio.persistence.postgresql.database=clinvio
clinvio.persistence.postgresql.username=postgres
clinvio.persistence.postgresql.password=your-password
clinvio.persistence.postgresql.ssl-mode=require
```

### MySQL

```properties
# application.properties
clinvio.persistence.type=mysql
clinvio.persistence.encryption-key=my-secret-key
clinvio.persistence.mysql.host=localhost
clinvio.persistence.mysql.port=3306
clinvio.persistence.mysql.database=clinvio
clinvio.persistence.mysql.username=clinvio_user
clinvio.persistence.mysql.password=your-password
clinvio.persistence.mysql.ssl-enabled=true
clinvio.persistence.mysql.ssl-mode=PREFERRED
```

### Oracle

```properties
# application.properties
clinvio.persistence.type=oracle
clinvio.persistence.encryption-key=my-secret-key
clinvio.persistence.oracle.host=localhost
clinvio.persistence.oracle.port=1521
clinvio.persistence.oracle.database=CLINVIO
clinvio.persistence.oracle.username=clinvio_user
clinvio.persistence.oracle.password=your-password
```

### SQL Server

```properties
# application.properties
clinvio.persistence.type=sqlserver
clinvio.persistence.encryption-key=my-secret-key
clinvio.persistence.sql-server.host=localhost
clinvio.persistence.sql-server.port=1433
clinvio.persistence.sql-server.database=CLINVIO
clinvio.persistence.sql-server.username=sa
clinvio.persistence.sql-server.password=your-password
clinvio.persistence.sql-server.encryption-enabled=false
```

---

## YAML Configuration Examples

### PostgreSQL (application.yml)

```yaml
clinvio:
  persistence:
    type: postgresql
    encryption-key: ${CLINVIO_ENCRYPTION_KEY}
    postgresql:
      host: localhost
      port: 5432
      database: clinvio
      username: postgres
      password: ${DB_PASSWORD}
      ssl-mode: require
```

### MySQL (application.yml)

```yaml
clinvio:
  persistence:
    type: mysql
    encryption-key: ${CLINVIO_ENCRYPTION_KEY}
    mysql:
      host: localhost
      port: 3306
      database: clinvio
      username: clinvio_user
      password: ${DB_PASSWORD}
      ssl-enabled: true
      ssl-mode: PREFERRED
```

---

## Environment Variables

All sensitive configuration can be set via environment variables:

```bash
# Required - Encryption key
export CLINVIO_ENCRYPTION_KEY="your-secret-key-min-32-chars"

# Database - PostgreSQL
export CLINVIO_PERSISTENCE_POSTGRESQL_HOST="db.example.com"
export CLINVIO_PERSISTENCE_POSTGRESQL_PORT="5432"
export CLINVIO_PERSISTENCE_POSTGRESQL_DATABASE="clinvio"
export CLINVIO_PERSISTENCE_POSTGRESQL_USERNAME="postgres"
export CLINVIO_PERSISTENCE_POSTGRESQL_PASSWORD="secure-password"
```

---

## Database-Specific Setup

### PostgreSQL Setup

```bash
# Create database
createdb -U postgres clinvio

# Create user
psql -U postgres -c "CREATE USER clinvio_user WITH PASSWORD 'password';"
psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE clinvio TO clinvio_user;"
```

### MySQL Setup

```bash
# Login to MySQL
mysql -u root -p

# Create database and user
CREATE DATABASE clinvio CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'clinvio_user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON clinvio.* TO 'clinvio_user'@'localhost';
FLUSH PRIVILEGES;
```

### Oracle Setup

```sql
-- Create tablespace
CREATE TABLESPACE clinvio_ts DATAFILE 'clinvio.dbf' SIZE 100M;

-- Create user
CREATE USER clinvio_user IDENTIFIED BY password;
GRANT CREATE SESSION, CREATE TABLE, CREATE SEQUENCE TO clinvio_user;
ALTER USER clinvio_user QUOTA 100M ON clinvio_ts;
```

### SQL Server Setup

```sql
-- Create database
CREATE DATABASE CLINVIO;

-- Create user
USE CLINVIO;
CREATE LOGIN [clinvio_user] WITH PASSWORD = 'SecurePassword123!';
CREATE USER [clinvio_user] FOR LOGIN [clinvio_user];
ALTER ROLE db_owner ADD MEMBER [clinvio_user];
```

---

## Docker Compose Examples

### PostgreSQL + Clinvio

```yaml
version: '3.8'
services:
  postgres:
    image: postgres:16-alpine
    environment:
      POSTGRES_DB: clinvio
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  clinvio:
    build: .
    environment:
      CLINVIO_PERSISTENCE_TYPE: postgresql
      CLINVIO_ENCRYPTION_KEY: change-in-production
      CLINVIO_PERSISTENCE_POSTGRESQL_HOST: postgres
      CLINVIO_PERSISTENCE_POSTGRESQL_PORT: 5432
      CLINVIO_PERSISTENCE_POSTGRESQL_DATABASE: clinvio
      CLINVIO_PERSISTENCE_POSTGRESQL_USERNAME: postgres
      CLINVIO_PERSISTENCE_POSTGRESQL_PASSWORD: postgres
    ports:
      - "8080:8080"
    depends_on:
      - postgres

volumes:
  postgres_data:
```

### MySQL + Clinvio

```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.3-alpine
    environment:
      MYSQL_DATABASE: clinvio
      MYSQL_ROOT_PASSWORD: root
      MYSQL_USER: clinvio_user
      MYSQL_PASSWORD: clinvio_password
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  clinvio:
    build: .
    environment:
      CLINVIO_PERSISTENCE_TYPE: mysql
      CLINVIO_ENCRYPTION_KEY: change-in-production
      CLINVIO_PERSISTENCE_MYSQL_HOST: mysql
      CLINVIO_PERSISTENCE_MYSQL_PORT: 3306
      CLINVIO_PERSISTENCE_MYSQL_DATABASE: clinvio
      CLINVIO_PERSISTENCE_MYSQL_USERNAME: clinvio_user
      CLINVIO_PERSISTENCE_MYSQL_PASSWORD: clinvio_password
    ports:
      - "8080:8080"
    depends_on:
      - mysql

volumes:
  mysql_data:
```

---

## Cloud Deployment Examples

### AWS RDS PostgreSQL

```properties
# application.properties
clinvio.persistence.type=postgresql
clinvio.persistence.encryption-key=${CLINVIO_ENCRYPTION_KEY}
clinvio.persistence.postgresql.host=mydb.c9akciq32.us-east-1.rds.amazonaws.com
clinvio.persistence.postgresql.port=5432
clinvio.persistence.postgresql.database=clinvio
clinvio.persistence.postgresql.username=postgres
clinvio.persistence.postgresql.password=${DB_PASSWORD}
clinvio.persistence.postgresql.ssl-mode=require
```

### Azure SQL Database

```properties
# application.properties
clinvio.persistence.type=sqlserver
clinvio.persistence.encryption-key=${CLINVIO_ENCRYPTION_KEY}
clinvio.persistence.sql-server.host=myserver.database.windows.net
clinvio.persistence.sql-server.port=1433
clinvio.persistence.sql-server.database=clinvio
clinvio.persistence.sql-server.username=clinvio_user@myserver
clinvio.persistence.sql-server.password=${DB_PASSWORD}
clinvio.persistence.sql-server.encryption-enabled=true
```

### Google Cloud SQL PostgreSQL

```properties
# application.properties
clinvio.persistence.type=postgresql
clinvio.persistence.encryption-key=${CLINVIO_ENCRYPTION_KEY}
clinvio.persistence.postgresql.host=35.227.1.1
clinvio.persistence.postgresql.port=5432
clinvio.persistence.postgresql.database=clinvio
clinvio.persistence.postgresql.username=postgres
clinvio.persistence.postgresql.password=${DB_PASSWORD}
clinvio.persistence.postgresql.ssl-mode=require
```

---

## Connection Pool Configuration

All databases use HikariCP for connection pooling. Configure with:

```properties
# application.properties - Optional pooling configuration
clinvio.connection-pool.maximum-pool-size=10
clinvio.connection-pool.minimum-idle=5
clinvio.connection-pool.idle-timeout=300000
clinvio.connection-pool.max-lifetime=1800000
clinvio.connection-pool.connection-timeout=30000
clinvio.connection-pool.pool-name=ClinvioPool
```

---

## Encryption Configuration

AES-256-GCM encryption works identically across all databases:

```properties
# Required - 32+ character key
clinvio.persistence.encryption-key=your-secret-key-must-be-32-chars-minimum

# Optional - custom salt (default: uses encryption-key for derivation)
clinvio.persistence.encryption-salt=optional-custom-salt

# Fields marked with @SensitiveData are encrypted automatically
```

Example entity:

```java
@Entity
public class Customer extends BaseEntity {
    
    private String customerId;  // NOT encrypted
    
    @SensitiveData
    @Convert(converter = EncryptedStringConverter.class)
    private String ssn;  // Encrypted in database
    
    @SensitiveData
    @Convert(converter = EncryptedStringConverter.class)
    private String email;  // Encrypted in database
}
```

---

## Migration Between Databases

### From SQLite to PostgreSQL

1. **Export data** from SQLite:
```bash
sqlite3 clinvio.db .dump > dump.sql
```

2. **Transform schema** for PostgreSQL (handle SQLite-specific syntax)

3. **Import to PostgreSQL**:
```bash
psql -U postgres -d clinvio -f dump.sql
```

4. **Update configuration**:
```properties
clinvio.persistence.type=postgresql
clinvio.persistence.postgresql.host=localhost
clinvio.persistence.postgresql.port=5432
clinvio.persistence.postgresql.database=clinvio
clinvio.persistence.postgresql.username=postgres
clinvio.persistence.postgresql.password=password
```

5. **Test thoroughly** - Ensure encryption still works for sensitive fields

---

## Testing with Different Databases

### Unit Tests (SQLite - Default)

```bash
# Tests use H2 in-memory database
mvn test
```

### Integration Tests (Optional - Requires Docker)

```bash
# Run with PostgreSQL container
mvn verify -Dtest=*PostgreSQL*

# Run with MySQL container  
mvn verify -Dtest=*MySQL*

# Run all container tests
mvn verify
```

---

## Troubleshooting

### "Database type not supported"

Ensure `clinvio.persistence.type` is one of: `sqlite`, `postgresql`, `mysql`, `oracle`, `sqlserver`

### "Connection refused"

- Verify database server is running
- Check host, port, username, password
- Ensure network/firewall allows connections

### "SSL error" (PostgreSQL)

```properties
# Try different SSL modes:
# disable, allow, prefer (default), require, verify-ca, verify-full
clinvio.persistence.postgresql.ssl-mode=disable
```

### "Unknown database" (MySQL)

```bash
# Verify database exists:
mysql -u root -p -e "SHOW DATABASES;"
```

### Encryption key mismatch

- Re-encrypted data is **unrecoverable** with different keys
- Always backup before changing `encryption-key`
- Use same key across all instances

---

## Performance Notes

### SQLite
- Best for: Single-user, embedded deployments
- Concurrent writes: Limited (single writer)
- Read replicas: Not supported

### PostgreSQL  
- Best for: Enterprise production
- Concurrent writes: Full ACID support
- Read replicas: Supported
- Performance: Excellent for large datasets

### MySQL
- Best for: Web applications
- Concurrent writes: Full ACID support (InnoDB)
- Read replicas: Supported
- Performance: Good for medium datasets

### Oracle
- Best for: Enterprise migrations
- Concurrent writes: Full ACID support
- Read replicas: Supported (Data Guard)
- Performance: Excellent for large datasets

### SQL Server
- Best for: Microsoft ecosystem
- Concurrent writes: Full ACID support
- Read replicas: Supported (Always On)
- Performance: Excellent for large datasets

---

## What's Next?

- [Security Best Practices](./SECURITY.md)
- [Deployment Guide](./DEPLOYMENT.md)
- [Performance Tuning](./PERFORMANCE.md)
- [Backup & Recovery](./BACKUP.md)

