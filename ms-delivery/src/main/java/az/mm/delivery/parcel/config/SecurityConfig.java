package az.mm.delivery.parcel.config;

import az.mm.delivery.common.security.TokenProvider;
import az.mm.delivery.common.security.config.JwtConfigurer;
import az.mm.delivery.common.security.handler.SecurityProblemHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import static az.mm.delivery.common.security.enums.Permissions.CHANGE_PARCEL_STATUS;
import static az.mm.delivery.common.security.enums.Permissions.COMPLETE_PARCEL_ORDER;
import static az.mm.delivery.common.security.enums.Permissions.RECEIVE_PARCEL;
import static az.mm.delivery.common.security.enums.Permissions.TRACK_PARCEL;
import static az.mm.delivery.common.security.enums.Permissions.UPDATE_PARCEL;
import static az.mm.delivery.common.security.enums.Permissions.VIEW_PARCEL;

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
            .antMatchers(HttpMethod.GET, "/api/parcels").hasAuthority(VIEW_PARCEL.get())
            .antMatchers(HttpMethod.GET, "/api/parcels/{id}").hasAuthority(VIEW_PARCEL.get())
            .antMatchers(HttpMethod.GET, "/api/parcels/track/{id}").hasAuthority(TRACK_PARCEL.get())
            .antMatchers(HttpMethod.PUT, "/api/parcels").hasAuthority(UPDATE_PARCEL.get())
            .antMatchers(HttpMethod.PUT, "/api/parcels/change-status").hasAuthority(CHANGE_PARCEL_STATUS.get())
            .antMatchers(HttpMethod.PUT, "/api/parcels/receive-parcel/{id}").hasAuthority(RECEIVE_PARCEL.get())
            .antMatchers(HttpMethod.PUT, "/api/parcels/complete-order/{id}").hasAuthority(COMPLETE_PARCEL_ORDER.get())

            .anyRequest().authenticated()

        .and()
            .apply(securityConfigurerAdapter());
        // @formatter:on
    }

    private JwtConfigurer securityConfigurerAdapter() {
        return new JwtConfigurer(tokenProvider);
    }

}
