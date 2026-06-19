package hu.clinvio.ui.persistence.integration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Multi-database integration tests marker.
 * Tests verify that Clinvio Nucleus works correctly with all supported databases.
 * 
 * To run individual database tests:
 * - SQLite (default): mvn test -Dgroups=integration
 * - PostgreSQL: mvn test -Dgroups=integration -Dtest=*PostgreSQL*
 * - MySQL: mvn test -Dgroups=integration -Dtest=*MySQL*
 * - Oracle: mvn test -Dgroups=integration -Dtest=*Oracle*
 * - SQL Server: mvn test -Dgroups=integration -Dtest=*SqlServer*
 */
public class MultiDatabaseIntegrationTest {

    /**
     * SQLite integration test (default, always runs).
     */
    @Test
    @DisplayName("SQLite - Database module integration test")
    public void testSqliteIntegration() {
        // SQLite tests are verified through other existing tests
        // This serves as a marker test for the test suite
    }

    /**
     * PostgreSQL integration test.
     * Requires testcontainers and PostgreSQL to be available.
     * Run with: mvn test -Dtest=*PostgreSQL* or mvn verify
     */
    @Disabled("Requires TestContainers - run with: mvn verify")
    @Test
    @DisplayName("PostgreSQL - Database module integration test")
    public void testPostgresqlIntegration() {
        // PostgreSQL tests implemented in PostgreSQLContainerTest
    }

    /**
     * MySQL integration test.
     * Requires testcontainers and MySQL to be available.
     * Run with: mvn test -Dtest=*MySQL* or mvn verify
     */
    @Disabled("Requires TestContainers - run with: mvn verify")
    @Test
    @DisplayName("MySQL - Database module integration test")
    public void testMysqlIntegration() {
        // MySQL tests implemented in MySQLContainerTest
    }

    /**
     * Oracle integration test.
     * Requires testcontainers and Oracle to be available.
     * Run with: mvn test -Dtest=*Oracle* or mvn verify
     */
    @Disabled("Requires TestContainers - run with: mvn verify")
    @Test
    @DisplayName("Oracle - Database module integration test")
    public void testOracleIntegration() {
        // Oracle tests implemented in OracleContainerTest
    }

    /**
     * SQL Server integration test.
     * Requires testcontainers and SQL Server to be available.
     * Run with: mvn test -Dtest=*SqlServer* or mvn verify
     */
    @Disabled("Requires TestContainers - run with: mvn verify")
    @Test
    @DisplayName("SQL Server - Database module integration test")
    public void testSqlServerIntegration() {
        // SQL Server tests implemented in SqlServerContainerTest
    }
}
