package hu.clinvio.ui.persistence.sqlite;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SQLiteDialectTest {

    @Test
    void dialectHasCorrectClassName() {
        SQLiteDialect dialect = new SQLiteDialect();
        assertEquals("hu.clinvio.ui.persistence.sqlite.SQLiteDialect", dialect.getClass().getName());
    }

    @Test
    void getDropForeignKeyStringReturnsEmpty() {
        SQLiteDialect dialect = new SQLiteDialect();
        assertEquals("", dialect.getDropForeignKeyString());
    }

    @Test
    void dialectSupportsIdentityColumns() {
        SQLiteDialect dialect = new SQLiteDialect();
        assertTrue(dialect.getIdentityColumnSupport().supportsIdentityColumns());
    }
}
