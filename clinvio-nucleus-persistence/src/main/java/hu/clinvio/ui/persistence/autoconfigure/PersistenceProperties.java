package hu.clinvio.ui.persistence.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Configuration properties for Clinvio persistence module.
 * Supports multiple databases: SQLite (default), PostgreSQL, MySQL, Oracle, SQL Server.
 *
 * <h3>Example application.properties (SQLite):</h3>
 * <pre>
 * clinvio.persistence.type=sqlite
 * clinvio.persistence.encryption-key=my-secret-key
 * clinvio.persistence.sqlite.database-path=./data/app.db
 * </pre>
 *
 * <h3>Example application.properties (PostgreSQL):</h3>
 * <pre>
 * clinvio.persistence.type=postgresql
 * clinvio.persistence.encryption-key=my-secret-key
 * clinvio.persistence.postgresql.host=localhost
 * clinvio.persistence.postgresql.port=5432
 * clinvio.persistence.postgresql.database=clinvio
 * clinvio.persistence.postgresql.username=postgres
 * clinvio.persistence.postgresql.password=password
 * </pre>
 *
 * <h3>Environment variable override:</h3>
 * <pre>
 * CLINVIO_ENCRYPTION_KEY=my-secret-key
 * </pre>
 */
@ConfigurationProperties(prefix = "clinvio.persistence")
public class PersistenceProperties {

    /**
     * Database type: sqlite, postgresql, mysql, oracle, sqlserver.
     * Defaults to sqlite for backward compatibility.
     */
    private String type = "sqlite";

    /**
     * Encryption key for AES-256-GCM encryption of sensitive data.
     * Can be overridden by the CLINVIO_ENCRYPTION_KEY environment variable.
     */
    private String encryptionKey;

    /**
     * PBKDF2 salt for AES key derivation. Override to rotate keys.
     * All existing encrypted data must be re-encrypted when changing salt.
     */
    private String encryptionSalt;

    /**
     * Whether to enable automatic DDL generation (create-drop).
     * Set to false for production use.
     */
    private boolean ddlAuto = true;

    @NestedConfigurationProperty
    private SqliteProperties sqlite = new SqliteProperties();

    @NestedConfigurationProperty
    private PostgresqlProperties postgresql = new PostgresqlProperties();

    @NestedConfigurationProperty
    private MysqlProperties mysql = new MysqlProperties();

    @NestedConfigurationProperty
    private OracleProperties oracle = new OracleProperties();

    @NestedConfigurationProperty
    private SqlServerProperties sqlServer = new SqlServerProperties();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public String getEncryptionSalt() {
        return encryptionSalt;
    }

    public void setEncryptionSalt(String encryptionSalt) {
        this.encryptionSalt = encryptionSalt;
    }

    public boolean isDdlAuto() {
        return ddlAuto;
    }

    public void setDdlAuto(boolean ddlAuto) {
        this.ddlAuto = ddlAuto;
    }

    public SqliteProperties getSqlite() {
        return sqlite;
    }

    public void setSqlite(SqliteProperties sqlite) {
        this.sqlite = sqlite;
    }

    public PostgresqlProperties getPostgresql() {
        return postgresql;
    }

    public void setPostgresql(PostgresqlProperties postgresql) {
        this.postgresql = postgresql;
    }

    public MysqlProperties getMysql() {
        return mysql;
    }

    public void setMysql(MysqlProperties mysql) {
        this.mysql = mysql;
    }

    public OracleProperties getOracle() {
        return oracle;
    }

    public void setOracle(OracleProperties oracle) {
        this.oracle = oracle;
    }

    public SqlServerProperties getSqlServer() {
        return sqlServer;
    }

    public void setSqlServer(SqlServerProperties sqlServer) {
        this.sqlServer = sqlServer;
    }

    /**
     * SQLite-specific configuration properties.
     */
    public static class SqliteProperties {
        private String databasePath = "./data/clinvio.db";

        public String getDatabasePath() {
            return databasePath;
        }

        public void setDatabasePath(String databasePath) {
            this.databasePath = databasePath;
        }
    }

    /**
     * PostgreSQL-specific configuration properties.
     */
    public static class PostgresqlProperties {
        private String host = "localhost";
        private int port = 5432;
        private String database = "clinvio";
        private String username;
        private String password;
        private String sslMode = "prefer";

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getDatabase() {
            return database;
        }

        public void setDatabase(String database) {
            this.database = database;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getSslMode() {
            return sslMode;
        }

        public void setSslMode(String sslMode) {
            this.sslMode = sslMode;
        }
    }

    /**
     * MySQL-specific configuration properties.
     */
    public static class MysqlProperties {
        private String host = "localhost";
        private int port = 3306;
        private String database = "clinvio";
        private String username;
        private String password;
        private boolean sslEnabled = true;
        private String sslMode = "PREFERRED";

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getDatabase() {
            return database;
        }

        public void setDatabase(String database) {
            this.database = database;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public boolean isSslEnabled() {
            return sslEnabled;
        }

        public void setSslEnabled(boolean sslEnabled) {
            this.sslEnabled = sslEnabled;
        }

        public String getSslMode() {
            return sslMode;
        }

        public void setSslMode(String sslMode) {
            this.sslMode = sslMode;
        }
    }

    /**
     * Oracle-specific configuration properties.
     */
    public static class OracleProperties {
        private String host = "localhost";
        private int port = 1521;
        private String database = "CLINVIO";
        private String username;
        private String password;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getDatabase() {
            return database;
        }

        public void setDatabase(String database) {
            this.database = database;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    /**
     * SQL Server-specific configuration properties.
     */
    public static class SqlServerProperties {
        private String host = "localhost";
        private int port = 1433;
        private String database = "CLINVIO";
        private String username;
        private String password;
        private boolean encryptionEnabled = true;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getDatabase() {
            return database;
        }

        public void setDatabase(String database) {
            this.database = database;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public boolean isEncryptionEnabled() {
            return encryptionEnabled;
        }

        public void setEncryptionEnabled(boolean encryptionEnabled) {
            this.encryptionEnabled = encryptionEnabled;
        }
    }
}
