package com.samoilov.project.antifraud.config;

import com.samoilov.project.antifraud.enums.Authority;
import com.samoilov.project.antifraud.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthService userDetailsService;

    private static final String[] PERMITTED_ALL_PATHS = new String[]{
            "/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };

    @Bean
    public SecurityFilterChain configure(
            HttpSecurity http,
            AuthenticationProvider authenticationProvider
    ) throws Exception {
        return http
                .authenticationProvider(authenticationProvider)
                .csrf(AbstractHttpConfigurer::disable) // disable CSRF to allow POST requests from Postman
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(PERMITTED_ALL_PATHS).permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/auth/user").permitAll();

                    auth.requestMatchers(HttpMethod.GET, "/api/auth/list").hasAnyAuthority(Authority.ADMINISTRATOR.getAuthority(), Authority.SUPPORT.getAuthority());
                    auth.requestMatchers(HttpMethod.POST, "/api/antifraud/transaction").hasAuthority(Authority.MERCHANT.getAuthority());
                    auth.requestMatchers(HttpMethod.PUT, "/api/auth/access").hasAuthority(Authority.ADMINISTRATOR.getAuthority());
                    auth.requestMatchers(HttpMethod.PUT, "/api/auth/role").hasAuthority(Authority.ADMINISTRATOR.getAuthority());
                    auth.requestMatchers(HttpMethod.DELETE, "/api/auth/user/{username}").hasAuthority(Authority.ADMINISTRATOR.getAuthority());
                    auth.requestMatchers(HttpMethod.GET, "/api/antifraud/suspicious-ip", "/api/antifraud/stolencard").hasAuthority(Authority.SUPPORT.getAuthority());
                    auth.requestMatchers(HttpMethod.POST, "/api/antifraud/suspicious-ip", "/api/antifraud/stolencard").hasAuthority(Authority.SUPPORT.getAuthority());
                    auth.requestMatchers(HttpMethod.DELETE, "/api/antifraud/suspicious-ip/{ip}", "/api/antifraud/stolencard/{cardNumber}").hasAuthority(Authority.SUPPORT.getAuthority());
                    auth.requestMatchers(HttpMethod.PUT, "/api/antifraud/transaction").hasAuthority(Authority.SUPPORT.getAuthority());
                    auth.requestMatchers(HttpMethod.GET, "/api/antifraud/history", "/api/antifraud/history/{number}").hasAuthority(Authority.SUPPORT.getAuthority());

                    auth.anyRequest().authenticated();
                })
                .sessionManagement(
                        httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ) // no session is created or used by Spring Security
                .build();
    }


    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

}
