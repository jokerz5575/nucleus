# Database Implementation Guide - PostgreSQL First

**Technical Implementation Roadmap for Multi-Database Support**

---

## Step 1: Prepare Configuration Layer

### 1.1 Extend PersistenceProperties.java

Add to existing file:

```java
package hu.clinvio.ui.persistence.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "clinvio.persistence")
public class PersistenceProperties {
    
    // EXISTING PROPERTIES - keep as-is
    private String encryptionKey;
    private String databasePath = "./data/clinvio.db";
    private String encryptionSalt;
    private boolean ddlAuto = true;
    
    // NEW: Database type selector
    private String databaseType = "sqlite"; // sqlite, postgresql, mysql, oracle
    
    // NEW: PostgreSQL configuration
    @NestedConfigurationProperty
    private PostgreSQLProperties postgresql = new PostgreSQLProperties();
    
    // NEW: MySQL configuration (for future)
    @NestedConfigurationProperty
    private MySQLProperties mysql = new MySQLProperties();
    
    // NEW: Oracle configuration (for future)
    @NestedConfigurationProperty
    private OracleProperties oracle = new OracleProperties();
    
    // Getters/Setters for databaseType
    public String getDatabaseType() { return databaseType; }
    public void setDatabaseType(String databaseType) { this.databaseType = databaseType; }
    
    // Getters for nested properties
    public PostgreSQLProperties getPostgresql() { return postgresql; }
    public void setPostgresql(PostgreSQLProperties postgresql) { this.postgresql = postgresql; }
    
    public MySQLProperties getMysql() { return mysql; }
    public void setMysql(MySQLProperties mysql) { this.mysql = mysql; }
    
    public OracleProperties getOracle() { return oracle; }
    public void setOracle(OracleProperties oracle) { this.oracle = oracle; }
    
    // NEW: Nested classes for each database
    public static class PostgreSQLProperties {
        private String host = "localhost";
        private int port = 5432;
        private String database = "clinvio";
        private String username;
        private String password;
        private boolean sslEnabled = false;
        private String sslMode = "disable"; // disable, allow, prefer, require
        
        // Getters/Setters
        public String getHost() { return host; }
        public void setHost(String host) { this.host = host; }
        
        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
        
        public String getDatabase() { return database; }
        public void setDatabase(String database) { this.database = database; }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        
        public boolean isSslEnabled() { return sslEnabled; }
        public void setSslEnabled(boolean sslEnabled) { this.sslEnabled = sslEnabled; }
        
        public String getSslMode() { return sslMode; }
        public void setSslMode(String sslMode) { this.sslMode = sslMode; }
    }
    
    public static class MySQLProperties {
        private String host = "localhost";
        private int port = 3306;
        private String database = "clinvio";
        private String username;
        private String password;
        private String charset = "utf8mb4";
        
        // Getters/Setters
        public String getHost() { return host; }
        public void setHost(String host) { this.host = host; }
        
        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
        
        public String getDatabase() { return database; }
        public void setDatabase(String database) { this.database = database; }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        
        public String getCharset() { return charset; }
        public void setCharset(String charset) { this.charset = charset; }
    }
    
    public static class OracleProperties {
        private String host = "localhost";
        private int port = 1521;
        private String service;
        private String sid;
        private String username;
        private String password;
        
        // Getters/Setters
        public String getHost() { return host; }
        public void setHost(String host) { this.host = host; }
        
        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
        
        public String getService() { return service; }
        public void setService(String service) { this.service = service; }
        
        public String getSid() { return sid; }
        public void setSid(String sid) { this.sid = sid; }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
```

---

## Step 2: Create DataSource Factory

### 2.1 Create DatabaseSourceFactory.java

New file: `hu/clinvio/ui/persistence/autoconfigure/DatabaseSourceFactory.java`

```java
package hu.clinvio.ui.persistence.autoconfigure;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

/**
 * Factory for creating database-specific DataSource instances.
 * Handles JDBC URL construction and driver selection for each database type.
 */
public class DatabaseSourceFactory {
    
    private static final Logger log = LoggerFactory.getLogger(DatabaseSourceFactory.class);
    
    /**
     * Creates a DataSource based on the configured database type.
     *
     * @param persistenceProps persistence configuration
     * @param poolProps connection pool configuration
     * @return configured DataSource
     * @throws IllegalArgumentException if database type is unsupported
     */
    public static DataSource createDataSource(
            PersistenceProperties persistenceProps,
            ConnectionPoolProperties poolProps) {
        
        String dbType = persistenceProps.getDatabaseType().toLowerCase();
        log.info("Creating DataSource for database type: {}", dbType);
        
        switch (dbType) {
            case "sqlite":
                return createSQLiteDataSource(persistenceProps, poolProps);
            case "postgresql":
                return createPostgresqlDataSource(persistenceProps, poolProps);
            case "mysql":
                return createMysqlDataSource(persistenceProps, poolProps);
            case "oracle":
                return createOracleDataSource(persistenceProps, poolProps);
            default:
                throw new IllegalArgumentException(
                    "Unsupported database type: " + persistenceProps.getDatabaseType() +
                    ". Supported types: sqlite, postgresql, mysql, oracle");
        }
    }
    
    /**
     * Creates SQLite DataSource.
     * File-based database suitable for development and small deployments.
     */
    private static DataSource createSQLiteDataSource(
            PersistenceProperties props,
            ConnectionPoolProperties pool) {
        HikariConfig config = new HikariConfig();
        
        String jdbcUrl = "jdbc:sqlite:" + props.getDatabasePath();
        config.setJdbcUrl(jdbcUrl);
        config.setDriverClassName("org.sqlite.JDBC");
        
        log.info("SQLite database configured at: {}", props.getDatabasePath());
        
        configurePool(config, pool);
        config.setMaximumPoolSize(5); // SQLite doesn't support high concurrency
        
        return new HikariDataSource(config);
    }
    
    /**
     * Creates PostgreSQL DataSource.
     * Enterprise-grade, production-ready database.
     */
    private static DataSource createPostgresqlDataSource(
            PersistenceProperties props,
            ConnectionPoolProperties pool) {
        
        var pgProps = props.getPostgresql();
        
        // Validate required properties
        if (pgProps.getUsername() == null || pgProps.getPassword() == null) {
            throw new IllegalStateException(
                "PostgreSQL database requires username and password. " +
                "Configure: clinvio.persistence.postgresql.username/password");
        }
        
        HikariConfig config = new HikariConfig();
        
        // Construct JDBC URL
        String sslMode = pgProps.isSslEnabled() ? 
            pgProps.getSslMode() : "disable";
        
        String jdbcUrl = String.format(
            "jdbc:postgresql://%s:%d/%s?sslmode=%s&ApplicationName=%s",
            pgProps.getHost(),
            pgProps.getPort(),
            pgProps.getDatabase(),
            sslMode,
            "clinvio-nucleus"
        );
        
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(pgProps.getUsername());
        config.setPassword(pgProps.getPassword());
        config.setDriverClassName("org.postgresql.Driver");
        
        log.info("PostgreSQL database configured at: {}:{}/{}",
            pgProps.getHost(), pgProps.getPort(), pgProps.getDatabase());
        
        configurePool(config, pool);
        config.setMaximumPoolSize(20); // PostgreSQL handles higher concurrency well
        
        // PostgreSQL-specific optimizations
        config.addDataSourceProperty("stringtype", "unspecified");
        config.addDataSourceProperty("reWriteBatchedInserts", "true");
        
        return new HikariDataSource(config);
    }
    
    /**
     * Creates MySQL DataSource.
     * Popular open-source database for web applications.
     */
    private static DataSource createMysqlDataSource(
            PersistenceProperties props,
            ConnectionPoolProperties pool) {
        
        var mysqlProps = props.getMysql();
        
        // Validate required properties
        if (mysqlProps.getUsername() == null || mysqlProps.getPassword() == null) {
            throw new IllegalStateException(
                "MySQL database requires username and password. " +
                "Configure: clinvio.persistence.mysql.username/password");
        }
        
        HikariConfig config = new HikariConfig();
        
        // Construct JDBC URL with UTF-8 and other recommended settings
        String jdbcUrl = String.format(
            "jdbc:mysql://%s:%d/%s?characterEncoding=%s&useUnicode=true" +
            "&serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false",
            mysqlProps.getHost(),
            mysqlProps.getPort(),
            mysqlProps.getDatabase(),
            mysqlProps.getCharset()
        );
        
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(mysqlProps.getUsername());
        config.setPassword(mysqlProps.getPassword());
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        
        log.info("MySQL database configured at: {}:{}/{}",
            mysqlProps.getHost(), mysqlProps.getPort(), mysqlProps.getDatabase());
        
        configurePool(config, pool);
        config.setMaximumPoolSize(20);
        
        // MySQL-specific optimizations
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        
        return new HikariDataSource(config);
    }
    
    /**
     * Creates Oracle DataSource.
     * Enterprise-grade database with advanced features.
     */
    private static DataSource createOracleDataSource(
            PersistenceProperties props,
            ConnectionPoolProperties pool) {
        
        var oracleProps = props.getOracle();
        
        // Validate required properties
        if (oracleProps.getUsername() == null || oracleProps.getPassword() == null) {
            throw new IllegalStateException(
                "Oracle database requires username and password. " +
                "Configure: clinvio.persistence.oracle.username/password");
        }
        
        HikariConfig config = new HikariConfig();
        
        // Construct JDBC URL - Oracle supports two formats
        String jdbcUrl;
        if (oracleProps.getService() != null && !oracleProps.getService().isBlank()) {
            // Using Service Name (recommended for Oracle 12c+)
            jdbcUrl = String.format(
                "jdbc:oracle:thin:@(DESCRIPTION=" +
                "(ADDRESS=(PROTOCOL=TCP)(HOST=%s)(PORT=%d))" +
                "(CONNECT_DATA=(SERVICE_NAME=%s)))",
                oracleProps.getHost(),
                oracleProps.getPort(),
                oracleProps.getService()
            );
        } else if (oracleProps.getSid() != null && !oracleProps.getSid().isBlank()) {
            // Using SID (legacy)
            jdbcUrl = String.format(
                "jdbc:oracle:thin:@%s:%d:%s",
                oracleProps.getHost(),
                oracleProps.getPort(),
                oracleProps.getSid()
            );
        } else {
            throw new IllegalStateException(
                "Oracle database requires either service or sid. " +
                "Configure: clinvio.persistence.oracle.service or clinvio.persistence.oracle.sid");
        }
        
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(oracleProps.getUsername());
        config.setPassword(oracleProps.getPassword());
        config.setDriverClassName("oracle.jdbc.OracleDriver");
        
        log.info("Oracle database configured at: {}:{}",
            oracleProps.getHost(), oracleProps.getPort());
        
        configurePool(config, pool);
        config.setMaximumPoolSize(25);
        
        // Oracle-specific optimizations
        config.addDataSourceProperty("useUnicode", "true");
        config.addDataSourceProperty("characterEncoding", "UTF-8");
        
        return new HikariDataSource(config);
    }
    
    /**
     * Configures common connection pool settings.
     */
    private static void configurePool(HikariConfig config, ConnectionPoolProperties pool) {
        config.setMaximumPoolSize(pool.getMaximumPoolSize());
        config.setMinimumIdle(pool.getMinimumIdle());
        config.setIdleTimeout(pool.getIdleTimeout());
        config.setMaxLifetime(pool.getMaxLifetime());
        config.setConnectionTimeout(pool.getConnectionTimeout());
        config.setPoolName(pool.getPoolName());
    }
}
```

---

## Step 3: Create Dialect Customizer Factory

### 3.1 Create HibernateDialectCustomizerFactory.java

New file: `hu/clinvio/ui/persistence/autoconfigure/HibernateDialectCustomizerFactory.java`

```java
package hu.clinvio.ui.persistence.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;

/**
 * Factory for creating Hibernate dialect customizers.
 * Registers the appropriate Hibernate dialect based on database type.
 */
public class HibernateDialectCustomizerFactory {
    
    private static final Logger log = LoggerFactory.getLogger(HibernateDialectCustomizerFactory.class);
    
    /**
     * Creates a HibernatePropertiesCustomizer for the specified database type.
     *
     * @param databaseType the database type (sqlite, postgresql, mysql, oracle)
     * @return a customizer that sets the appropriate Hibernate dialect
     * @throws IllegalArgumentException if database type is unsupported
     */
    public static HibernatePropertiesCustomizer createDialectCustomizer(String databaseType) {
        return hibernateProperties -> {
            String dialect = getDialectClass(databaseType);
            log.info("Configuring Hibernate dialect: {}", dialect);
            hibernateProperties.put("hibernate.dialect", dialect);
        };
    }
    
    /**
     * Returns the Hibernate dialect class for the given database type.
     *
     * @param databaseType the database type
     * @return the fully-qualified dialect class name
     */
    private static String getDialectClass(String databaseType) {
        switch (databaseType.toLowerCase()) {
            case "sqlite":
                return "org.hibernate.community.dialect.SQLiteDialect";
                
            case "postgresql":
                // PostgreSQL 15+ recommended for best compatibility
                return "org.hibernate.dialect.PostgreSQL15Dialect";
                
            case "mysql":
                // MySQL 8.0+ recommended
                return "org.hibernate.dialect.MySQL8Dialect";
                
            case "oracle":
                // Oracle Database 19c+ recommended
                return "org.hibernate.dialect.OracleDialect";
                
            default:
                throw new IllegalArgumentException(
                    "Unsupported database type: " + databaseType +
                    ". Supported types: sqlite, postgresql, mysql, oracle");
        }
    }
}
```

---

## Step 4: Update Auto-Configuration

### 4.1 Modify PersistenceAutoConfiguration.java

Replace the existing `dataSource()` and `sqliteHibernateCustomizer()` beans:

```java
/**
 * Creates a DataSource based on the configured database type.
 * Supports SQLite, PostgreSQL, MySQL, and Oracle.
 */
@Bean
@ConditionalOnMissingBean
@ConditionalOnClass(HikariConfig.class)
public DataSource dataSource(ConnectionPoolProperties poolProps, 
        PersistenceProperties props) {
    
    return DatabaseSourceFactory.createDataSource(props, poolProps);
}

/**
 * Configures Hibernate dialect based on database type.
 * Called after DataSource is created.
 */
@Bean
public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(
        PersistenceProperties props) {
    
    return HibernateDialectCustomizerFactory
        .createDialectCustomizer(props.getDatabaseType());
}
```

---

## Step 5: Configuration Examples

### 5.1 application.properties - PostgreSQL

```properties
# Database Type
clinvio.persistence.database-type=postgresql

# PostgreSQL Configuration
clinvio.persistence.postgresql.host=localhost
clinvio.persistence.postgresql.port=5432
clinvio.persistence.postgresql.database=clinvio
clinvio.persistence.postgresql.username=postgres
clinvio.persistence.postgresql.password=your_password
clinvio.persistence.postgresql.ssl-enabled=false

# Encryption
clinvio.persistence.encryption-key=your-encryption-key
clinvio.persistence.encryption-salt=optional-salt

# Connection Pool
clinvio.nucleus.datasource.maximum-pool-size=20
clinvio.nucleus.datasource.minimum-idle=5
clinvio.nucleus.datasource.idle-timeout=300000
clinvio.nucleus.datasource.max-lifetime=1800000
clinvio.nucleus.datasource.connection-timeout=30000
clinvio.nucleus.datasource.pool-name=clinvio-pool
```

### 5.2 application-prod.yml - PostgreSQL Production

```yaml
clinvio:
  persistence:
    database-type: postgresql
    encryption-key: ${ENCRYPTION_KEY}
    encryption-salt: ${ENCRYPTION_SALT}
    ddl-auto: false  # Never auto-generate DDL in production
    postgresql:
      host: ${DB_HOST}
      port: 5432
      database: ${DB_NAME}
      username: ${DB_USER}
      password: ${DB_PASSWORD}
      ssl-enabled: true
      ssl-mode: require
  
  nucleus:
    datasource:
      maximum-pool-size: 25
      minimum-idle: 10
      connection-timeout: 30000
      max-lifetime: 1800000
      pool-name: clinvio-prod-pool
```

### 5.3 docker-compose.yml - PostgreSQL Development

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15-alpine
    container_name: clinvio-postgres
    environment:
      POSTGRES_DB: clinvio
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: develop
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres"]
      interval: 10s
      timeout: 5s
      retries: 5

  clinvio-app:
    build: .
    container_name: clinvio-app
    environment:
      DB_HOST: postgres
      DB_PORT: 5432
      DB_NAME: clinvio
      DB_USER: postgres
      DB_PASSWORD: develop
      ENCRYPTION_KEY: dev-key-change-in-production
    ports:
      - "8080:8080"
    depends_on:
      postgres:
        condition: service_healthy

volumes:
  postgres_data:
```

---

## Step 6: Testing

### 6.1 PostgreSQL Integration Test

Create: `src/test/java/.../persistence/postgresql/PostgreSQLIntegrationTest.java`

```java
package hu.clinvio.ui.persistence.postgresql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@TestPropertySource(properties = {
    "clinvio.persistence.database-type=postgresql",
    "clinvio.persistence.encryption-key=test-key-12345"
})
class PostgreSQLIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = 
        new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("clinvio_test")
            .withUsername("test")
            .withPassword("test");
    
    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("clinvio.persistence.postgresql.host", postgres::getHost);
        registry.add("clinvio.persistence.postgresql.port", () -> postgres.getFirstMappedPort());
        registry.add("clinvio.persistence.postgresql.database", postgres::getDatabaseName);
        registry.add("clinvio.persistence.postgresql.username", postgres::getUsername);
        registry.add("clinvio.persistence.postgresql.password", postgres::getPassword);
    }
    
    @Autowired
    private DataSource dataSource;
    
    @BeforeEach
    void setUp() {
        assertNotNull(dataSource, "DataSource should be auto-configured");
    }
    
    @Test
    void testDatabaseConnection() throws Exception {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT 1")) {
            
            assertTrue(rs.next(), "Query should return a result");
            assertEquals(1, rs.getInt(1), "Query should return 1");
        }
    }
    
    @Test
    void testPostgreSQLDialectConfigured() {
        // Test that PostgreSQL dialect is properly configured
        assertTrue(postgres.isRunning(), "PostgreSQL container should be running");
    }
}
```

---

## Step 7: Dependencies

### 7.1 Add PostgreSQL Driver to pom.xml

Add to `clinvio-nucleus-persistence/pom.xml`:

```xml
<!-- PostgreSQL JDBC Driver -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.0</version>
</dependency>

<!-- MySQL Driver (optional, for future) -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.2.0</version>
    <optional>true</optional>
</dependency>

<!-- Oracle Driver (optional, for future) -->
<!-- Requires Oracle Maven repository configuration -->
<!--
<dependency>
    <groupId>com.oracle.database.jdbc</groupId>
    <artifactId>ojdbc8</artifactId>
    <version>23.4.0.24.05</version>
    <optional>true</optional>
</dependency>
-->

<!-- TestContainers for PostgreSQL testing -->
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>postgresql</artifactId>
    <version>1.19.0</version>
    <scope>test</scope>
</dependency>
```

---

## Step 8: Documentation

### 8.1 Configuration Reference

Create: `docs/database-configuration.md`

```markdown
# Database Configuration Guide

## Supported Databases

- SQLite (default, no configuration needed)
- PostgreSQL (recommended for production)
- MySQL (supported)
- Oracle (supported)

## Database Type Property

Set `clinvio.persistence.database-type` to:
- `sqlite` - File-based SQLite database (default)
- `postgresql` - PostgreSQL 12+
- `mysql` - MySQL 8.0+
- `oracle` - Oracle 19c+

## PostgreSQL Configuration

Required properties:
- `clinvio.persistence.postgresql.host` - Database host (default: localhost)
- `clinvio.persistence.postgresql.port` - Database port (default: 5432)
- `clinvio.persistence.postgresql.database` - Database name (default: clinvio)
- `clinvio.persistence.postgresql.username` - Database user
- `clinvio.persistence.postgresql.password` - Database password

Optional properties:
- `clinvio.persistence.postgresql.ssl-enabled` - Enable SSL (default: false)
- `clinvio.persistence.postgresql.ssl-mode` - SSL mode: disable, allow, prefer, require

Example:
```properties
clinvio.persistence.database-type=postgresql
clinvio.persistence.postgresql.host=db.example.com
clinvio.persistence.postgresql.port=5432
clinvio.persistence.postgresql.database=myapp
clinvio.persistence.postgresql.username=postgres
clinvio.persistence.postgresql.password=secret
```
```

---

## Step 9: Validation Checklist

Before releasing PostgreSQL support:

- [ ] All tests pass with PostgreSQL
- [ ] Performance benchmarks completed
- [ ] Connection pooling optimized per database
- [ ] Encryption works identically
- [ ] Soft delete behavior verified
- [ ] Concurrent access tested
- [ ] Migration guides written
- [ ] Docker Compose examples provided
- [ ] Configuration documentation complete
- [ ] JDBC driver properly licensed
- [ ] README updated with PostgreSQL
- [ ] CI/CD pipeline includes PostgreSQL tests
- [ ] Backward compatibility maintained (SQLite still works)

---

## Step 10: Rollout Strategy

### Phase 1: Development (Internal)
1. Implement factory classes
2. Add PostgreSQL configuration
3. Run integration tests
4. Benchmark performance

### Phase 2: Beta Testing
1. Deploy to staging with PostgreSQL
2. Run production-like workloads
3. Monitor for issues
4. Collect feedback

### Phase 3: General Availability (GA)
1. Release with full documentation
2. Add support for MySQL
3. Plan for Oracle/SQL Server

---

## Troubleshooting

### PostgreSQL Connection Refused
```
Error: Could not connect to database

Solutions:
1. Check PostgreSQL is running
2. Verify host, port, database name
3. Check username/password
4. Verify firewall allows port 5432
5. Check pg_hba.conf authentication
```

### Wrong Dialect Error
```
Error: No Dialect mapping for JDBC type: ...

Solutions:
1. Verify clinvio.persistence.database-type=postgresql
2. Check that PostgreSQL JDBC driver is in classpath
3. Verify Hibernate 6.x is being used
```

### Performance Issues
```
Solutions:
1. Check connection pool size is appropriate
2. Monitor database logs
3. Run EXPLAIN ANALYZE on slow queries
4. Check database statistics are up-to-date
5. Consider indexing strategies
```

---

**Next Step:** Follow this guide to implement PostgreSQL support incrementally.

