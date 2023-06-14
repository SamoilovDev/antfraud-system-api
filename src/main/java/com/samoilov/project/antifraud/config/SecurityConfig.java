package com.samoilov.project.antifraud.config;

import com.samoilov.project.antifraud.enums.Authority;
import com.samoilov.project.antifraud.handler.RestAuthenticationEntryPoint;
import com.samoilov.project.antifraud.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    private final AuthService userDetailsService;

    private static final String[] SWAGGER_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
    };

    @Bean
    public SecurityFilterChain configure(
            HttpSecurity http,
            DaoAuthenticationProvider daoAuthenticationProvider
    ) throws Exception {
        return http
                .userDetailsService(userDetailsService)
                .authenticationProvider(daoAuthenticationProvider)
                .httpBasic(
                        httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer
                                .authenticationEntryPoint(restAuthenticationEntryPoint)
                )
                .csrf(AbstractHttpConfigurer::disable) // disable CSRF to allow POST requests from Postman
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(SWAGGER_WHITELIST).permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/auth/user").permitAll();
                    auth.requestMatchers("/swagger-resources/*", "*.html", "/api/v1/swagger.json", "/webjars/**", "/swagger-ui/**").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/auth/list").hasAnyAuthority(Authority.ADMINISTRATOR.getAuthority(), Authority.SUPPORT.getAuthority());
                    auth.requestMatchers(HttpMethod.POST, "/api/antifraud/transaction").hasAuthority(Authority.MERCHANT.getAuthority());
                    auth.requestMatchers(HttpMethod.PUT, "/api/auth/access").hasAuthority(Authority.ADMINISTRATOR.getAuthority());
                    auth.requestMatchers(HttpMethod.PUT, "/api/auth/role").hasAuthority(Authority.ADMINISTRATOR.getAuthority());
                    auth.requestMatchers(HttpMethod.DELETE, "/api/auth/user/{username}").hasAuthority(Authority.ADMINISTRATOR.getAuthority());
                    auth.requestMatchers(HttpMethod.GET, "/api/antifraud/suspicious-ip","/api/antifraud/stolencard").hasAuthority(Authority.SUPPORT.getAuthority());
                    auth.requestMatchers(HttpMethod.POST,"/api/antifraud/suspicious-ip", "/api/antifraud/stolencard").hasAuthority(Authority.SUPPORT.getAuthority());
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
    public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(encoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }


}
