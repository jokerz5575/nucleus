package hu.clinvio.ui.persistence.autoconfigure;

import com.zaxxer.hikari.HikariConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory for creating HikariConfig based on database type.
 * Constructs appropriate JDBC URLs and driver class names for each database.
 */
public class DatabaseSourceFactory {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSourceFactory.class);

    /**
     * Create a HikariConfig for the configured database type.
     */
    public static HikariConfig createConfig(PersistenceProperties props, ConnectionPoolProperties poolProps) {
        HikariConfig config = new HikariConfig();
        
        String type = props.getType().toLowerCase().trim();
        
        switch (type) {
            case "sqlite":
                configureSqlite(config, props, poolProps);
                break;
            case "postgresql":
            case "postgres":
                configurePostgresql(config, props, poolProps);
                break;
            case "mysql":
                configureMysql(config, props, poolProps);
                break;
            case "oracle":
                configureOracle(config, props, poolProps);
                break;
            case "sqlserver":
            case "sql-server":
            case "mssql":
                configureSqlServer(config, props, poolProps);
                break;
            default:
                throw new IllegalArgumentException("Unsupported database type: " + type);
        }
        
        configurePooling(config, poolProps);
        return config;
    }

    /**
     * Determine the Hibernate dialect for the configured database type.
     */
    public static String getHibernateDialect(PersistenceProperties props) {
        String type = props.getType().toLowerCase().trim();
        
        switch (type) {
            case "sqlite":
                return "org.hibernate.community.dialect.SQLiteDialect";
            case "postgresql":
            case "postgres":
                return "org.hibernate.dialect.PostgreSQLDialect";
            case "mysql":
                return "org.hibernate.dialect.MySQL8Dialect";
            case "oracle":
                return "org.hibernate.dialect.OracleDialect";
            case "sqlserver":
            case "sql-server":
            case "mssql":
                return "org.hibernate.dialect.SQLServerDialect";
            default:
                throw new IllegalArgumentException("Unsupported database type: " + type);
        }
    }

    /**
     * Get the JDBC driver class name for the configured database type.
     */
    public static String getDriverClassName(PersistenceProperties props) {
        String type = props.getType().toLowerCase().trim();
        
        switch (type) {
            case "sqlite":
                return "org.sqlite.JDBC";
            case "postgresql":
            case "postgres":
                return "org.postgresql.Driver";
            case "mysql":
                return "com.mysql.cj.jdbc.Driver";
            case "oracle":
                return "oracle.jdbc.OracleDriver";
            case "sqlserver":
            case "sql-server":
            case "mssql":
                return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            default:
                throw new IllegalArgumentException("Unsupported database type: " + type);
        }
    }

    private static void configureSqlite(HikariConfig config, PersistenceProperties props, ConnectionPoolProperties poolProps) {
        String databasePath = props.getSqlite().getDatabasePath();
        config.setJdbcUrl("jdbc:sqlite:" + databasePath);
        config.setDriverClassName("org.sqlite.JDBC");
        log.info("Configured SQLite database: {}", databasePath);
    }

    private static void configurePostgresql(HikariConfig config, PersistenceProperties props, ConnectionPoolProperties poolProps) {
        PersistenceProperties.PostgresqlProperties pg = props.getPostgresql();
        String url = String.format("jdbc:postgresql://%s:%d/%s?sslmode=%s",
                pg.getHost(), pg.getPort(), pg.getDatabase(), pg.getSslMode());
        config.setJdbcUrl(url);
        config.setDriverClassName("org.postgresql.Driver");
        config.setUsername(pg.getUsername());
        config.setPassword(pg.getPassword());
        log.info("Configured PostgreSQL database: {}@{}:{}/{}", 
                pg.getUsername(), pg.getHost(), pg.getPort(), pg.getDatabase());
    }

    private static void configureMysql(HikariConfig config, PersistenceProperties props, ConnectionPoolProperties poolProps) {
        PersistenceProperties.MysqlProperties mysql = props.getMysql();
        String sslString = mysql.isSslEnabled() ? "?sslMode=" + mysql.getSslMode() : "";
        String url = String.format("jdbc:mysql://%s:%d/%s%s&allowPublicKeyRetrieval=true&useSSL=%s",
                mysql.getHost(), mysql.getPort(), mysql.getDatabase(), 
                sslString, mysql.isSslEnabled());
        config.setJdbcUrl(url);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setUsername(mysql.getUsername());
        config.setPassword(mysql.getPassword());
        log.info("Configured MySQL database: {}@{}:{}/{}", 
                mysql.getUsername(), mysql.getHost(), mysql.getPort(), mysql.getDatabase());
    }

    private static void configureOracle(HikariConfig config, PersistenceProperties props, ConnectionPoolProperties poolProps) {
        PersistenceProperties.OracleProperties oracle = props.getOracle();
        String url = String.format("jdbc:oracle:thin:@%s:%d:%s",
                oracle.getHost(), oracle.getPort(), oracle.getDatabase());
        config.setJdbcUrl(url);
        config.setDriverClassName("oracle.jdbc.OracleDriver");
        config.setUsername(oracle.getUsername());
        config.setPassword(oracle.getPassword());
        log.info("Configured Oracle database: {}@{}:{}", 
                oracle.getUsername(), oracle.getHost(), oracle.getPort());
    }

    private static void configureSqlServer(HikariConfig config, PersistenceProperties props, ConnectionPoolProperties poolProps) {
        PersistenceProperties.SqlServerProperties sqlServer = props.getSqlServer();
        String encryptStr = sqlServer.isEncryptionEnabled() ? "true" : "false";
        String url = String.format("jdbc:sqlserver://%s:%d;database=%s;encrypt=%s;trustServerCertificate=false;connection Timeout=30000",
                sqlServer.getHost(), sqlServer.getPort(), sqlServer.getDatabase(), encryptStr);
        config.setJdbcUrl(url);
        config.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        config.setUsername(sqlServer.getUsername());
        config.setPassword(sqlServer.getPassword());
        log.info("Configured SQL Server database: {}@{}:{}/{}", 
                sqlServer.getUsername(), sqlServer.getHost(), sqlServer.getPort(), sqlServer.getDatabase());
    }

    private static void configurePooling(HikariConfig config, ConnectionPoolProperties poolProps) {
        if (config.getUsername() == null || config.getUsername().isEmpty()) {
            // For SQLite, username/password are not needed
            config.setUsername("");
            config.setPassword("");
        }
        config.setMaximumPoolSize(poolProps.getMaximumPoolSize());
        config.setMinimumIdle(poolProps.getMinimumIdle());
        config.setIdleTimeout(poolProps.getIdleTimeout());
        config.setMaxLifetime(poolProps.getMaxLifetime());
        config.setConnectionTimeout(poolProps.getConnectionTimeout());
        config.setPoolName(poolProps.getPoolName());
    }
}
