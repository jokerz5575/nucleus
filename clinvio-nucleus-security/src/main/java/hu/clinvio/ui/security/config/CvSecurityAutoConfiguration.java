package hu.clinvio.ui.security.config;

import hu.clinvio.ui.security.controller.CvAuthController;
import hu.clinvio.ui.security.controller.CvCsrfController;
import hu.clinvio.ui.security.filter.CvJwtAuthenticationFilter;
import hu.clinvio.ui.security.filter.CvSecuredInterceptor;
import hu.clinvio.ui.security.service.CvJwtService;
import hu.clinvio.ui.security.service.TokenBlacklistService;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

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
    public TokenBlacklistService tokenBlacklistService() {
        return new TokenBlacklistService();
    }

    @Bean
    @ConditionalOnMissingBean
    public CvJwtAuthenticationFilter cvJwtAuthenticationFilter(CvJwtService jwtService,
                                                                SecurityProperties properties,
                                                                TokenBlacklistService blacklistService) {
        return new CvJwtAuthenticationFilter(jwtService, properties, blacklistService);
    }

    @Bean
    @ConditionalOnMissingBean
    public CvAuthController cvAuthController(CvJwtService jwtService, TokenBlacklistService blacklistService) {
        return new CvAuthController(jwtService, blacklistService);
    }

    @Bean
    @ConditionalOnMissingBean
    public CvCsrfController cvCsrfController(SecurityProperties properties) {
        return new CvCsrfController(properties);
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
    public CorsConfigurationSource corsConfigurationSource(SecurityProperties properties) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(properties.getCors().getAllowedOrigins()));
        configuration.setAllowedMethods(List.of(properties.getCors().getAllowedMethods()));
        configuration.setAllowedHeaders(List.of(properties.getCors().getAllowedHeaders()));
        configuration.setAllowCredentials(properties.getCors().isAllowCredentials());
        configuration.setMaxAge(properties.getCors().getMaxAge());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    @ConditionalOnMissingBean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   SecurityProperties properties,
                                                   CvJwtAuthenticationFilter jwtFilter,
                                                   CorsConfigurationSource corsSource) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsSource))
            .csrf(csrf -> {
                if (properties.getCsrf().isEnabled()) {
                    csrf.ignoringRequestMatchers(properties.getPermitPaths());
                } else {
                    csrf.disable();
                }
            })
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers(properties.getPermitPaths()).permitAll();
                auth.anyRequest().authenticated();
            })
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
