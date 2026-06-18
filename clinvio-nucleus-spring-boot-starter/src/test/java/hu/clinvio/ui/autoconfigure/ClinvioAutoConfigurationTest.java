package hu.clinvio.ui.autoconfigure;

import hu.clinvio.ui.core.dialect.ClinvioDialect;
import hu.clinvio.ui.core.event.CvEventBus;
import hu.clinvio.ui.core.registry.CvComponentRegistry;
import hu.clinvio.ui.core.renderer.CvRenderer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.TemplateEngine;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {ClinvioAutoConfiguration.class, ClinvioAutoConfigurationTest.TestConfig.class})
class ClinvioAutoConfigurationTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        TemplateEngine templateEngine() {
            return new TemplateEngine();
        }
    }

    @Autowired
    private ClinvioAutoConfiguration autoConfiguration;

    @Autowired(required = false)
    private ClinvioDialect clinvioDialect;

    @Autowired(required = false)
    private CvComponentRegistry cvComponentRegistry;

    @Autowired(required = false)
    private CvEventBus cvEventBus;

    @Autowired(required = false)
    private CvRenderer cvRenderer;

    @Test
    void contextLoads() {
        assertNotNull(autoConfiguration);
    }

    @Test
    void clinvioDialectBeanIsCreated() {
        assertNotNull(clinvioDialect);
    }

    @Test
    void cvComponentRegistryBeanIsCreated() {
        assertNotNull(cvComponentRegistry);
    }

    @Test
    void cvEventBusBeanIsCreated() {
        assertNotNull(cvEventBus);
    }

    @Test
    void cvRendererBeanIsCreated() {
        assertNotNull(cvRenderer);
    }
}
