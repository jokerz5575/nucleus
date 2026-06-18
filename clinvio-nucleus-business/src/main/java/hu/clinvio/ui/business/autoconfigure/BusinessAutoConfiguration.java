package hu.clinvio.ui.business.autoconfigure;

import hu.clinvio.ui.business.exception.CvExceptionHandler;
import hu.clinvio.ui.business.response.HxResponseHandler;
import hu.clinvio.ui.core.renderer.CvRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Auto-configuration for Clinvio business logic layer.
 * Registers the HxResponseHandler and global exception handler.
 */
@AutoConfiguration
public class BusinessAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(BusinessAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(CvRenderer.class)
    public HxResponseHandler hxResponseHandler(CvRenderer renderer) {
        log.info("Registering HxResponse handler for HTMX fragment rendering");
        return new HxResponseHandler(renderer);
    }

    @Bean
    @ConditionalOnMissingBean
    public CvExceptionHandler cvExceptionHandler() {
        return new CvExceptionHandler();
    }

    @Bean
    public WebMvcConfigurer businessMvcConfigurer(HxResponseHandler hxResponseHandler) {
        return new WebMvcConfigurer() {
            @Override
            public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
                handlers.add(hxResponseHandler);
            }
        };
    }
}
