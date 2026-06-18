package hu.clinvio.ui.components.config;

import hu.clinvio.ui.core.component.CvComponentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Auto-registers all component templates with the Thymeleaf dialect.
 * Scans classpath for cv/components/*.html templates.
 */
@Component
public class ComponentAutoRegistrar {

    private static final Logger log = LoggerFactory.getLogger(ComponentAutoRegistrar.class);

    @EventListener(ApplicationReadyEvent.class)
    public void registerComponents() {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:/templates/cv/components/*.html");
            
            List<String> templates = new ArrayList<>();
            for (Resource resource : resources) {
                String filename = resource.getFilename();
                if (filename != null && filename.endsWith(".html")) {
                    String name = filename.replace(".html", "");
                    templates.add(name);
                    log.debug("Registered component template: cv/components/{}", name);
                }
            }
            
            log.info("Auto-registered {} component templates: {}", templates.size(), templates);
        } catch (Exception e) {
            log.warn("Could not auto-register component templates: {}", e.getMessage());
        }
    }
}
