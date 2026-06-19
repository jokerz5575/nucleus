package hu.clinvio.ui.persistence.autoconfigure;

import hu.clinvio.ui.persistence.crypto.AesCryptoService;
import hu.clinvio.ui.persistence.crypto.EncryptedStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Auto-configuration for Clinvio persistence module.
 * Sets up AES encryption service and configures SQLite dialect for Hibernate.
 */
@AutoConfiguration
@EnableConfigurationProperties({PersistenceProperties.class, ConnectionPoolProperties.class})
@EnableJpaRepositories(basePackages = {"hu.clinvio.ui"})
@ComponentScan(basePackages = {"hu.clinvio.ui"})
@org.springframework.boot.persistence.autoconfigure.EntityScan(basePackages = {"hu.clinvio.ui"})
public class PersistenceAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(PersistenceAutoConfiguration.class);
    private static final String[] PRODUCTION_PROFILES = {"prod", "production"};

    @Bean
    @ConditionalOnMissingBean
    public AesCryptoService aesCryptoService(PersistenceProperties properties, Environment environment) {
        String key = environment.getProperty("CLINVIO_ENCRYPTION_KEY");
        if (key == null || key.isBlank()) {
            key = properties.getEncryptionKey();
        }

        if (key == null || key.isBlank()) {
            boolean isProduction = Arrays.stream(environment.getActiveProfiles())
                    .anyMatch(p -> Arrays.asList(PRODUCTION_PROFILES).contains(p));

            if (isProduction) {
                throw new IllegalStateException(
                        "FATAL: No encryption key configured for production environment. " +
                        "Set clinvio.persistence.encryption-key or CLINVIO_ENCRYPTION_KEY.");
            }

            log.warn("No encryption key configured. Using development key. DO NOT use in production.");
            key = "clinvio-dev-key-change-in-production";
        }

        // Auto-create data directory for SQLite
        String dbPath = properties.getDatabasePath();
        if (dbPath != null && !dbPath.isBlank()) {
            try {
                Path parent = Paths.get(dbPath).getParent();
                if (parent != null && !Files.exists(parent)) {
                    Files.createDirectories(parent);
                    log.info("Created data directory: {}", parent);
                }
            } catch (Exception e) {
                log.warn("Could not create data directory: {}", e.getMessage());
            }
        }

        log.info("Initializing AES-256-GCM crypto service");

        String saltStr = properties.getEncryptionSalt();
        byte[] salt = saltStr != null && !saltStr.isBlank()
                ? saltStr.getBytes(java.nio.charset.StandardCharsets.UTF_8)
                : null;
        return new AesCryptoService(key, salt);
    }

    @Bean
    @ConditionalOnMissingBean
    public EncryptedStringConverter encryptedStringConverter(AesCryptoService cryptoService) {
        EncryptedStringConverter converter = new EncryptedStringConverter();
        converter.setCryptoService(cryptoService);
        return converter;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(HikariConfig.class)
    public DataSource dataSource(ConnectionPoolProperties poolProps, PersistenceProperties props) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:" + props.getDatabasePath());
        config.setMaximumPoolSize(poolProps.getMaximumPoolSize());
        config.setMinimumIdle(poolProps.getMinimumIdle());
        config.setIdleTimeout(poolProps.getIdleTimeout());
        config.setMaxLifetime(poolProps.getMaxLifetime());
        config.setConnectionTimeout(poolProps.getConnectionTimeout());
        config.setPoolName(poolProps.getPoolName());
        config.setDriverClassName("org.sqlite.JDBC");
        return new HikariDataSource(config);
    }

    @Bean
    public HibernatePropertiesCustomizer sqliteHibernateCustomizer(PersistenceProperties props) {
        return hibernateProperties -> {
            hibernateProperties.put("hibernate.dialect", "org.hibernate.community.dialect.SQLiteDialect");
            if (props.isDdlAuto()) {
                hibernateProperties.put("hibernate.hbm2ddl.auto", "update");
            }
        };
    }
}
