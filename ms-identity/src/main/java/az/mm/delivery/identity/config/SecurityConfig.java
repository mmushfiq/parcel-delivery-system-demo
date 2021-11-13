package az.mm.delivery.identity.config;

import az.mm.delivery.common.security.TokenProvider;
import az.mm.delivery.common.security.config.JwtConfigurer;
import az.mm.delivery.common.security.handler.SecurityProblemHandler;
import az.mm.delivery.identity.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static az.mm.delivery.common.security.enums.Permissions.ADD_PERMISSION;
import static az.mm.delivery.common.security.enums.Permissions.ADD_ROLE;
import static az.mm.delivery.common.security.enums.Permissions.ADD_USER;
import static az.mm.delivery.common.security.enums.Permissions.CREATE_COURIER;
import static az.mm.delivery.common.security.enums.Permissions.DELETE_PERMISSION;
import static az.mm.delivery.common.security.enums.Permissions.DELETE_ROLE;
import static az.mm.delivery.common.security.enums.Permissions.DELETE_USER;
import static az.mm.delivery.common.security.enums.Permissions.UPDATE_PERMISSION;
import static az.mm.delivery.common.security.enums.Permissions.UPDATE_ROLE;
import static az.mm.delivery.common.security.enums.Permissions.UPDATE_USER;
import static az.mm.delivery.common.security.enums.Permissions.VIEW_COURIER;
import static az.mm.delivery.common.security.enums.Permissions.VIEW_PERMISSION;
import static az.mm.delivery.common.security.enums.Permissions.VIEW_ROLE;
import static az.mm.delivery.common.security.enums.Permissions.VIEW_USER;
import static az.mm.delivery.common.security.enums.Permissions.VIEW_USER_LIST;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;
    private final SecurityProblemHandler securityProblemHandler;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider());
    }

    @Override
    public void configure(WebSecurity web) {
        /*
         * JwtTokenFilter doesn't work for these endpoints
         */
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/actuator/health")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/swagger-ui/**")
                .antMatchers("/v2/api-docs/**")
                .antMatchers("/v3/api-docs/**")
                .antMatchers("/webjars/**")
                .antMatchers("/favicon.ico")
                .antMatchers("/error")
                .antMatchers("/auth/signup")
                .antMatchers("/auth/signin")
                .antMatchers("/auth/refresh");
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
            .antMatchers("/auth/signup").permitAll()
            .antMatchers("/auth/signin").permitAll()
            .antMatchers("/auth/refresh").permitAll()

            .antMatchers(HttpMethod.POST, "/auth/signup/courier").hasAuthority(CREATE_COURIER.get())

            .antMatchers(HttpMethod.GET, "/api/permissions**").hasAuthority(VIEW_PERMISSION.get())
            .antMatchers(HttpMethod.POST, "/api/permissions").hasAuthority(ADD_PERMISSION.get())
            .antMatchers(HttpMethod.PUT, "/api/permissions").hasAuthority(UPDATE_PERMISSION.get())
            .antMatchers(HttpMethod.DELETE, "/api/permissions/{id}").hasAuthority(DELETE_PERMISSION.get())

            .antMatchers(HttpMethod.GET, "/api/roles**").hasAuthority(VIEW_ROLE.get())
            .antMatchers(HttpMethod.POST, "/api/roles").hasAuthority(ADD_ROLE.get())
            .antMatchers(HttpMethod.PUT, "/api/roles").hasAuthority(UPDATE_ROLE.get())
            .antMatchers(HttpMethod.DELETE, "/api/roles/{id}").hasAuthority(DELETE_ROLE.get())

            .antMatchers(HttpMethod.GET, "/api/users").hasAuthority(VIEW_USER_LIST.get())
            .antMatchers(HttpMethod.GET, "/api/users/{id}").hasAuthority(VIEW_USER.get())
            .antMatchers(HttpMethod.GET, "/api/users/couriers").hasAuthority(VIEW_COURIER.get())
            .antMatchers(HttpMethod.POST, "/api/users").hasAuthority(ADD_USER.get())
            .antMatchers(HttpMethod.PUT, "/api/users").hasAuthority(UPDATE_USER.get())
            .antMatchers(HttpMethod.DELETE, "/api/users/{id}").hasAuthority(DELETE_USER.get())

            .anyRequest().authenticated()

        .and()
            .apply(securityConfigurerAdapter());
        // @formatter:on
    }

    private JwtConfigurer securityConfigurerAdapter() {
        return new JwtConfigurer(tokenProvider);
    }

}
