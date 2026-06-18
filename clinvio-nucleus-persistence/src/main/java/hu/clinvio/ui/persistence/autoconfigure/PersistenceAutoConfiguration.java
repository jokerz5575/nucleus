package hu.clinvio.ui.persistence.autoconfigure;

import hu.clinvio.ui.persistence.crypto.AesCryptoService;
import hu.clinvio.ui.persistence.crypto.EncryptedStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Auto-configuration for Clinvio persistence module.
 * Sets up AES encryption service and configures SQLite dialect for Hibernate.
 */
@AutoConfiguration
@EnableConfigurationProperties(PersistenceProperties.class)
@EnableJpaRepositories(basePackages = {"hu.clinvio.ui", "hu.clinvio.nucleus"})
@ComponentScan(basePackages = {"hu.clinvio.ui", "hu.clinvio.nucleus"})
@org.springframework.boot.autoconfigure.domain.EntityScan(basePackages = {"hu.clinvio.ui", "hu.clinvio.nucleus"})
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
        return new AesCryptoService(key);
    }

    @Bean
    @ConditionalOnMissingBean
    public EncryptedStringConverter encryptedStringConverter(AesCryptoService cryptoService) {
        EncryptedStringConverter converter = new EncryptedStringConverter();
        converter.setCryptoService(cryptoService);
        return converter;
    }

    @Bean
    public HibernatePropertiesCustomizer sqliteHibernateCustomizer() {
        return hibernateProperties -> {
            hibernateProperties.put("hibernate.dialect", "org.hibernate.community.dialect.SQLiteDialect");
        };
    }
}
