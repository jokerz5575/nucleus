package hu.clinvio.ui.persistence.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Clinvio persistence module.
 *
 * <h3>Example application.properties:</h3>
 * <pre>
 * clinvio.persistence.encryption-key=my-secret-key
 * clinvio.persistence.database-path=./data/app.db
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
     * Encryption key for AES-256-GCM encryption of sensitive data.
     * Can be overridden by the CLINVIO_ENCRYPTION_KEY environment variable.
     */
    private String encryptionKey;

    /**
     * Path to the SQLite database file.
     * Defaults to ./data/clinvio.db in the working directory.
     */
    private String databasePath = "./data/clinvio.db";

    /**
     * Whether to enable automatic DDL generation (create-drop).
     * Set to false for production use.
     */
    private boolean ddlAuto = true;

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public String getDatabasePath() {
        return databasePath;
    }

    public void setDatabasePath(String databasePath) {
        this.databasePath = databasePath;
    }

    public boolean isDdlAuto() {
        return ddlAuto;
    }

    public void setDdlAuto(boolean ddlAuto) {
        this.ddlAuto = ddlAuto;
    }
}
