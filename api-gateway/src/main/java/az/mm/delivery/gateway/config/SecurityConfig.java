package az.mm.delivery.gateway.config;

import az.mm.delivery.common.security.handler.SecurityProblemHandler;
import az.mm.delivery.gateway.config.properties.ApplicationProperties;
import az.mm.delivery.gateway.security.JwtVerifyConfigurer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

@EnableWebSecurity
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityProblemHandler securityProblemHandler;
    private final HandlerExceptionResolver exceptionResolver;
    private final ApplicationProperties properties;

    @Autowired
    public SecurityConfig(SecurityProblemHandler securityProblemHandler,
                          @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver,
                          ApplicationProperties properties) {
        this.securityProblemHandler = securityProblemHandler;
        this.exceptionResolver = exceptionResolver;
        this.properties = properties;
    }

    @Override
    public void configure(WebSecurity webSecurity) {
        /*
            CORS doesn't work for endpoints which added to webSecurity->ignoring,
            we should use httpSecurity->permitAll() for them.
         */
        webSecurity.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/")
                .antMatchers("/actuator/health")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/swagger-ui/**")
                .antMatchers("/swagger**")
                .antMatchers("/v2/api-docs/**")
                .antMatchers("/v3/api-docs/**")
                .antMatchers("/error")
                .antMatchers("/webjars/**")
                .antMatchers("/favicon.ico");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        // @formatter:off
        httpSecurity
            .cors()

        .and()
            .csrf().disable()
            .exceptionHandling()
                .authenticationEntryPoint(securityProblemHandler)
                .accessDeniedHandler(securityProblemHandler)

        .and()
            .headers()
            .frameOptions()
            .sameOrigin()

        .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        .and()
            .authorizeRequests()
            .antMatchers("/identity/**").permitAll()
            .antMatchers("/order/**").permitAll()
            .antMatchers("/delivery/**").permitAll()
            .antMatchers("/notification/**").permitAll()

            .anyRequest().authenticated()

        .and()
            .apply(securityConfigurerAdapter());
        // @formatter:on
    }

    private JwtVerifyConfigurer securityConfigurerAdapter() {
        return new JwtVerifyConfigurer(exceptionResolver, properties);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfig = properties.getCors();
        if (!CollectionUtils.isEmpty(corsConfig.getAllowedOrigins())) {
            log.debug("Registering CORS filter; " +
                            "\n allowedOrigins: {}, \n allowedHeaders: {}, \n allowedMethods: {}",
                    corsConfig.getAllowedOrigins(), corsConfig.getAllowedHeaders(), corsConfig.getAllowedMethods());
            source.registerCorsConfiguration("/**", corsConfig);
        }
        return source;
    }

}
