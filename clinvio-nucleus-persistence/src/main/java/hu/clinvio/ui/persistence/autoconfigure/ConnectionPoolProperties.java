package hu.clinvio.ui.persistence.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "clinvio.nucleus.datasource")
public class ConnectionPoolProperties {
    private int maximumPoolSize = 10;
    private int minimumIdle = 5;
    private long idleTimeout = 300000;
    private long maxLifetime = 1800000;
    private long connectionTimeout = 30000;
    private String poolName = "clinvio-pool";

    public int getMaximumPoolSize() { return maximumPoolSize; }
    public void setMaximumPoolSize(int maximumPoolSize) { this.maximumPoolSize = maximumPoolSize; }
    public int getMinimumIdle() { return minimumIdle; }
    public void setMinimumIdle(int minimumIdle) { this.minimumIdle = minimumIdle; }
    public long getIdleTimeout() { return idleTimeout; }
    public void setIdleTimeout(long idleTimeout) { this.idleTimeout = idleTimeout; }
    public long getMaxLifetime() { return maxLifetime; }
    public void setMaxLifetime(long maxLifetime) { this.maxLifetime = maxLifetime; }
    public long getConnectionTimeout() { return connectionTimeout; }
    public void setConnectionTimeout(long connectionTimeout) { this.connectionTimeout = connectionTimeout; }
    public String getPoolName() { return poolName; }
    public void setPoolName(String poolName) { this.poolName = poolName; }
}
