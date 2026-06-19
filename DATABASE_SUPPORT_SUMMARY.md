# Clinvio Nucleus - Database Support Summary

**Quick Reference for Adding Database Support**

---

## Current Status

- **Supported Databases:** SQLite only (3.53.2.0)
- **Framework:** Spring Boot 4.1.0, Hibernate 6.x, Spring Data JPA 3.x
- **Architecture Quality:** Excellent (4-level abstraction layer)
- **Encryption:** AES-256-GCM at JPA converter level (database-agnostic)

---

## Key Findings

### Excellent News ✅

1. **Database abstraction is production-ready**
   - All queries use JPQL (database-agnostic)
   - No native SQL dependencies
   - Clean separation of concerns

2. **Adding new databases is straightforward**
   - PostgreSQL: 3-5 days development
   - MySQL: 3-5 days development
   - Oracle: 5-7 days development
   - SQL Server: 5-7 days development

3. **Encryption works with any database**
   - Field-level AES-256-GCM implemented at application layer
   - Not database-specific
   - Compatible with all JDBC databases

4. **Testing infrastructure ready**
   - H2 in-memory database already in tests
   - Proves JDBC abstraction works
   - TestContainers-compatible architecture

### Current Limitations ⚠️

1. **SQLite-specific constraints**
   - Single writer concurrency limit
   - No ALTER TABLE support
   - No foreign key enforcement
   - No full-text search

2. **Enterprise features blocked**
   - No read replicas
   - No horizontal scaling
   - No advanced monitoring
   - Limited failover options

---

## Implementation Effort

### Quick Add: PostgreSQL Only (Fastest Path)

```
Week 1-2:   Architecture refactoring         (40-80 hours)
Week 3-4:   PostgreSQL implementation        (80-120 hours)
Week 5-6:   Testing & integration tests      (80-120 hours)
Week 7:     Documentation & Docker setup     (40 hours)
────────────────────────────────────────────────────────
Total:      4-6 weeks                        (240-360 hours)
```

### Full Implementation: PostgreSQL + MySQL

```
Week 1-2:   Architecture refactoring         (40-80 hours)
Week 3-4:   PostgreSQL implementation        (80-120 hours)
Week 5-6:   PostgreSQL testing               (80-120 hours)
Week 7-8:   MySQL implementation             (80-120 hours)
Week 9-10:  MySQL testing                    (80-120 hours)
Week 11:    Documentation & examples         (40 hours)
────────────────────────────────────────────────────────
Total:      8-12 weeks                       (480-680 hours)
```

---

## Recommended Priorities

### Priority 1: PostgreSQL ⭐⭐⭐⭐⭐
- Most requested for enterprise deployments
- Excellent cloud support (AWS RDS, Azure, Google Cloud)
- Strong Java ecosystem
- Low risk implementation
- **Recommendation:** Release as v2.5.0

### Priority 2: MySQL ⭐⭐⭐⭐
- High adoption in web applications
- Docker-friendly, easy testing
- Wide cloud availability
- Low-medium risk (charset handling)
- **Recommendation:** Release as v2.6.0

### Priority 3: Oracle ⭐⭐⭐
- Large enterprise deployments
- Complex sequence/ID handling
- Medium-high implementation risk
- **Recommendation:** Consider based on demand

### Priority 4: SQL Server ⭐⭐⭐
- Azure ecosystem value
- Windows authentication option
- Medium-high implementation risk
- **Recommendation:** Consider based on demand

---

## Architecture Overview

### Current (SQLite-Only)

```
Application
    ↓
CvBaseRepository (Spring Data JPA)
    ↓
Hibernate EntityManager
    ↓
HibernatePropertiesCustomizer
    ├── SQLiteDialect (custom)
    └── HikariDataSource
        └── jdbc:sqlite:path/to/db
```

### Proposed (Multi-Database)

```
Application
    ↓
CvBaseRepository (Spring Data JPA)
    ↓
Hibernate EntityManager
    ↓
HibernateDialectCustomizerFactory
    ├── SQLiteDialect
    ├── PostgreSQL15Dialect
    ├── MySQL8Dialect
    └── OracleDialect

DatabaseSourceFactory
    ├── SQLite JDBC URL
    ├── PostgreSQL JDBC URL
    ├── MySQL JDBC URL
    └── Oracle JDBC URL
        ↓
    HikariDataSource (Connection Pool)
```

---

## Code Changes Required

### 1. Extended PersistenceProperties

```java
// Add new property
private String databaseType = "sqlite"; // sqlite, postgresql, mysql, oracle

// Add nested configs
private PostgreSQLProperties postgresql = new PostgreSQLProperties();
private MySQLProperties mysql = new MySQLProperties();
private OracleProperties oracle = new OracleProperties();
```

### 2. Create Factory Classes

```java
DatabaseSourceFactory.createDataSource(databaseType)
    ↓
    Switch on database type and create JDBC URL + driver

HibernateDialectCustomizerFactory.createDialectCustomizer(databaseType)
    ↓
    Switch on database type and register Hibernate dialect
```

### 3. Update Auto-Configuration

```java
@Bean
public DataSource dataSource(...) {
    return DatabaseSourceFactory.createDataSource(props, poolProps);
}

@Bean
public HibernatePropertiesCustomizer hibernateCustomizer(...) {
    return HibernateDialectCustomizerFactory.createDialectCustomizer(...);
}
```

---

## Configuration Examples

### PostgreSQL

```properties
clinvio.persistence.database-type=postgresql
clinvio.persistence.postgresql.host=localhost
clinvio.persistence.postgresql.port=5432
clinvio.persistence.postgresql.database=clinvio
clinvio.persistence.postgresql.username=postgres
clinvio.persistence.postgresql.password=secret
clinvio.persistence.postgresql.ssl-enabled=true
```

### MySQL

```properties
clinvio.persistence.database-type=mysql
clinvio.persistence.mysql.host=localhost
clinvio.persistence.mysql.port=3306
clinvio.persistence.mysql.database=clinvio
clinvio.persistence.mysql.username=root
clinvio.persistence.mysql.password=secret
clinvio.persistence.mysql.charset=utf8mb4
```

### SQLite (Default/Backward Compatible)

```properties
clinvio.persistence.database-type=sqlite
clinvio.persistence.database-path=./data/clinvio.db
```

---

## Testing Strategy

### Per-Database Integration Tests

```java
@SpringBootTest
@TestcontainersTest
class PostgreSQLIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgres = 
        new PostgreSQLContainer<>(DockerImageName.parse("postgres:15"));
    
    @Test void testEntityPersistenceWithEncryption() { ... }
    @Test void testSoftDeleteBehavior() { ... }
    @Test void testConcurrentAccess() { ... }
}
```

### CI/CD Test Matrix

```yaml
test-postgresql:
  services:
    postgres: image: postgres:15

test-mysql:
  services:
    mysql: image: mysql:8

test-sqlite:
  # No service needed (file-based)
```

---

## Risk Assessment

| Database | Risk Level | Complexity | Benefits | Notes |
|----------|-----------|-----------|----------|-------|
| **PostgreSQL** | 🟢 Low | Medium | High | Recommended first choice |
| **MySQL** | 🟡 Low-Medium | Medium | High | Charset handling care needed |
| **Oracle** | 🟡 Medium | High | Medium | Sequence complexity |
| **SQL Server** | 🟡 Medium | High | Medium | Azure integration value |

---

## Migration Path: SQLite → PostgreSQL

### Step 1: Export Data
```bash
# Use JPA repositories to export entities
EntityRepository.findAll().forEach(entity -> {
    // Serialize to migration format
});
```

### Step 2: Create Target Schema
```sql
-- PostgreSQL schema auto-created by Hibernate
-- Using same entities, just different dialect
```

### Step 3: Import Data
```bash
# Use Spring Batch or direct insertion
```

### Step 4: Update Connection
```properties
# Change database type in properties
clinvio.persistence.database-type=postgresql
```

### Downtime: Minimal (hours during off-peak)

---

## What Works Across All Databases ✅

- All JPA entity relationships
- All JPA annotations
- BaseEntity audit fields (createdAt, updatedAt, deletedAt)
- Soft delete patterns
- AES-256-GCM encrypted fields
- Spring Data JPA repositories
- Pagination and sorting
- JPQL queries

---

## What Needs Adjustment Per Database ⚠️

| Item | SQLite | PostgreSQL | MySQL | Oracle |
|------|--------|-----------|-------|--------|
| ID Generation | Identity | Identity | Identity | Sequence |
| String Length | No limit | No limit | Max 65535 | No limit |
| Character Set | UTF-8 | UTF-8 | utf8mb4 (needed) | NLS_LANG |
| Timezone | Local | UTC recommended | UTC (config) | NLS_DATE_FORMAT |

---

## Recommended Execution Plan

### Phase 1: Foundations (Week 1-2)
- Refactor PersistenceProperties
- Create DatabaseSourceFactory
- Create HibernateDialectCustomizerFactory
- Add multi-database test infrastructure

### Phase 2: PostgreSQL (Week 3-6)
- Add PostgreSQL dialect configuration
- Add PostgreSQL DataSource builder
- Implement full integration tests
- Add Docker Compose setup
- Document PostgreSQL usage

### Phase 3: MySQL (Week 7-10)
- Add MySQL dialect configuration
- Add MySQL DataSource builder
- Implement integration tests
- Add Docker Compose setup
- Document MySQL usage

### Phase 4: Polish (Week 11-12)
- Performance benchmarking
- Migration guides
- Examples and sample apps
- Update README and roadmap

---

## Success Metrics

✅ All databases pass same test suite  
✅ Zero performance degradation vs SQLite  
✅ Encryption works identically on all databases  
✅ Configuration is intuitive and documented  
✅ Migration guides available from SQLite  
✅ Performance benchmarks documented  
✅ CI/CD tests all supported databases  

---

## References

- **Full Analysis:** See `clinvio_database_analysis.md` (39KB comprehensive report)
- **Key Files:** 
  - PersistenceAutoConfiguration.java
  - PersistenceProperties.java
  - SQLiteDialect.java
  - CvBaseRepository.java
- **Tests:** SQLiteDialectTest.java, SensitiveDataJpaEncryptionTest.java

---

## Decision Matrix

**Should we add multi-database support?**

| Factor | Score | Notes |
|--------|-------|-------|
| **Effort** | ✅ Low (3-5 days per DB) | Straightforward with existing architecture |
| **Risk** | ✅ Low | JDBC abstraction proven by H2 tests |
| **Market Value** | ✅✅✅ High | Enterprise blocker currently |
| **Implementation Quality** | ✅✅✅ High | Clean existing patterns to follow |
| **Testing Feasibility** | ✅ Good | TestContainers support proven |

**Recommendation:** ✅ **Proceed with PostgreSQL as v2.5.0 priority**

---

**Document Version:** 1.0  
**Date:** June 19, 2026  
**Status:** Ready for Implementation Planning  
**Confidence Level:** High
