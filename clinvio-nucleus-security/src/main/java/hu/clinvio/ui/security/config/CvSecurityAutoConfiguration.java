package hu.clinvio.ui.security.config;

import hu.clinvio.ui.security.filter.CvJwtAuthenticationFilter;
import hu.clinvio.ui.security.filter.CvSecuredInterceptor;
import hu.clinvio.ui.security.service.CvJwtService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AutoConfiguration
@EnableConfigurationProperties(SecurityProperties.class)
@ConditionalOnProperty(prefix = "clinvio.security", name = "enabled", matchIfMissing = true)
public class CvSecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean
    public CvJwtService cvJwtService(SecurityProperties properties) {
        return new CvJwtService(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public CvJwtAuthenticationFilter cvJwtAuthenticationFilter(CvJwtService jwtService, SecurityProperties properties) {
        return new CvJwtAuthenticationFilter(jwtService, properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public CvSecuredInterceptor cvSecuredInterceptor() {
        return new CvSecuredInterceptor();
    }

    @Bean
    @ConditionalOnWebApplication
    public WebMvcConfigurer securityWebMvcConfigurer(CvSecuredInterceptor securedInterceptor) {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(securedInterceptor);
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   SecurityProperties properties,
                                                   CvJwtAuthenticationFilter jwtFilter) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers(properties.getPermitPaths()).permitAll();
                auth.anyRequest().authenticated();
            })
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
