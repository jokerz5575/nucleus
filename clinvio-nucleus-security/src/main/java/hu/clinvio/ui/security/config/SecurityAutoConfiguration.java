package hu.clinvio.ui.security.config;

import hu.clinvio.ui.security.service.CvJwtService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Auto-configuration for Clinvio Security.
 */
@AutoConfiguration
@EnableConfigurationProperties(SecurityProperties.class)
@ConditionalOnProperty(prefix = "clinvio.security", name = "enabled", matchIfMissing = true)
public class SecurityAutoConfiguration {

    @Bean
    @org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
    public CvJwtService cvJwtService(SecurityProperties properties) {
        return new CvJwtService(properties);
    }
}
