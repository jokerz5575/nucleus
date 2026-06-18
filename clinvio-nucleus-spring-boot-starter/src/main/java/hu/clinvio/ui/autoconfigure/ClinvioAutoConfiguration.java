package hu.clinvio.ui.autoconfigure;

import hu.clinvio.ui.core.dialect.ClinvioDialect;
import hu.clinvio.ui.core.event.CvEventBus;
import hu.clinvio.ui.core.registry.CvComponentRegistry;
import hu.clinvio.ui.core.renderer.CvRenderer;
import hu.clinvio.ui.htmx.filter.HxRequestFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.TemplateEngine;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Auto-configuration for Clinvio UI Framework.
 * Automatically sets up the dialect, component registry, renderer, and HTMX filter.
 */
@AutoConfiguration(after = ThymeleafAutoConfiguration.class)
@ConditionalOnWebApplication
@ConditionalOnClass(ClinvioDialect.class)
@ConditionalOnProperty(prefix = ClinvioProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(ClinvioProperties.class)
public class ClinvioAutoConfiguration implements WebMvcConfigurer {

    private final ClinvioProperties properties;

    public ClinvioAutoConfiguration(ClinvioProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public ClinvioDialect clinvioDialect() {
        return new ClinvioDialect();
    }

    @Bean
    @ConditionalOnMissingBean
    public CvComponentRegistry cvComponentRegistry() {
        return new CvComponentRegistry();
    }

    @Bean
    @ConditionalOnMissingBean
    public CvEventBus cvEventBus() {
        return new CvEventBus();
    }

    @Bean
    @ConditionalOnMissingBean
    public CvRenderer cvRenderer(TemplateEngine templateEngine, CvComponentRegistry componentRegistry) {
        return new CvRenderer(templateEngine, componentRegistry);
    }

    @Bean
    @ConditionalOnProperty(prefix = ClinvioProperties.PREFIX + ".htmx", name = "enabled", matchIfMissing = true)
    public FilterRegistrationBean<HxRequestFilter> hxRequestFilterRegistration() {
        FilterRegistrationBean<HxRequestFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new HxRequestFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        registration.setName("hxRequestFilter");
        return registration;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve Clinvio UI static resources
        registry.addResourceHandler("/cv/css/**")
                .addResourceLocations("classpath:/static/cv/css/");
        registry.addResourceHandler("/cv/js/**")
                .addResourceLocations("classpath:/static/cv/js/");
        registry.addResourceHandler("/cv/fonts/**")
                .addResourceLocations("classpath:/static/cv/fonts/");
    }
}
