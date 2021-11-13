package az.mm.delivery.order.config;

import az.mm.delivery.common.security.TokenProvider;
import az.mm.delivery.common.security.config.JwtConfigurer;
import az.mm.delivery.common.security.handler.SecurityProblemHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import static az.mm.delivery.common.security.enums.Permissions.ACCEPT_ORDER;
import static az.mm.delivery.common.security.enums.Permissions.ASSIGN_COURIER;
import static az.mm.delivery.common.security.enums.Permissions.CANCEL_ORDER;
import static az.mm.delivery.common.security.enums.Permissions.CHANGE_ORDER_DESTINATION;
import static az.mm.delivery.common.security.enums.Permissions.CHANGE_ORDER_STATUS;
import static az.mm.delivery.common.security.enums.Permissions.CREATE_ORDER;
import static az.mm.delivery.common.security.enums.Permissions.UPDATE_ORDER;
import static az.mm.delivery.common.security.enums.Permissions.VIEW_ORDER;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;
    private final SecurityProblemHandler securityProblemHandler;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/actuator/health")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/swagger-ui/**")
                .antMatchers("/v2/api-docs/**")
                .antMatchers("/v3/api-docs/**")
                .antMatchers("/webjars/**")
                .antMatchers("/favicon.ico")
                .antMatchers("/error");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        // @formatter:off
        httpSecurity
            .headers()
            .contentSecurityPolicy("script-src 'self'");

        httpSecurity
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

            .antMatchers(HttpMethod.GET, "/api/orders").hasAuthority(VIEW_ORDER.get())
            .antMatchers(HttpMethod.GET, "/api/orders/{id}").hasAuthority(VIEW_ORDER.get())
            .antMatchers(HttpMethod.POST, "/api/orders").hasAuthority(CREATE_ORDER.get())
            .antMatchers(HttpMethod.PUT, "/api/orders").hasAuthority(UPDATE_ORDER.get())
            .antMatchers(HttpMethod.PUT, "/api/orders/change-destination").hasAuthority(CHANGE_ORDER_DESTINATION.get())
            .antMatchers(HttpMethod.PUT, "/api/orders/cancel/{id}").hasAuthority(CANCEL_ORDER.get())
            .antMatchers(HttpMethod.PUT, "/api/orders/accept/{id}").hasAuthority(ACCEPT_ORDER.get())
            .antMatchers(HttpMethod.PUT, "/api/orders/change-status").hasAuthority(CHANGE_ORDER_STATUS.get())
            .antMatchers(HttpMethod.PUT, "/api/orders/assign-courier").hasAuthority(ASSIGN_COURIER.get())

            .anyRequest().authenticated()

        .and()
            .apply(securityConfigurerAdapter());
        // @formatter:on
    }

    private JwtConfigurer securityConfigurerAdapter() {
        return new JwtConfigurer(tokenProvider);
    }

}
