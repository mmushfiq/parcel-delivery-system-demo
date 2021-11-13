package az.mm.delivery.gateway.security;

import az.mm.delivery.gateway.config.properties.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@RequiredArgsConstructor
public class JwtVerifyConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final ApplicationProperties properties;

    @Override
    public void configure(HttpSecurity httpSecurity) {
        JwtVerifyFilter jwtVerifyFilter = new JwtVerifyFilter(handlerExceptionResolver, properties);
        httpSecurity.addFilterBefore(jwtVerifyFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
