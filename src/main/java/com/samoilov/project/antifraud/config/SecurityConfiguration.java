package com.samoilov.project.antifraud.config;

import com.samoilov.project.antifraud.enums.Authority;
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
public class SecurityConfiguration {

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    private final AuthService userDetailsService;

    @Bean
    public SecurityFilterChain configure(
            HttpSecurity http,
            DaoAuthenticationProvider daoAuthenticationProvider
    ) throws Exception {
        return http.userDetailsService(userDetailsService)
                .authenticationProvider(daoAuthenticationProvider)
                .httpBasic(
                        httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer
                                .authenticationEntryPoint(restAuthenticationEntryPoint)
                )
                .csrf(AbstractHttpConfigurer::disable) // disable CSRF to allow POST requests from Postman
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/actuator/shutdown").permitAll(); // needs to run tests
                    auth.requestMatchers(HttpMethod.POST, "/api/auth/user").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/auth/list").hasAnyAuthority(Authority.ADMINISTRATOR.getAuthority(), Authority.SUPPORT.getAuthority());
                    auth.requestMatchers(HttpMethod.POST, "/api/antifraud/transaction").hasAuthority(Authority.MERCHANT.getAuthority());
                    auth.requestMatchers(HttpMethod.PUT, "/api/auth/access").hasAuthority(Authority.ADMINISTRATOR.getAuthority());
                    auth.requestMatchers(HttpMethod.PUT, "/api/auth/role").hasAuthority(Authority.ADMINISTRATOR.getAuthority());
                    auth.requestMatchers(HttpMethod.DELETE, "/api/auth/user/{username}").hasAuthority(Authority.ADMINISTRATOR.getAuthority());
                    auth.requestMatchers(HttpMethod.GET, "/api/antifraud/suspicious-ip","/api/antifraud/stolencard").hasAuthority(Authority.SUPPORT.getAuthority());
                    auth.requestMatchers(HttpMethod.POST,"/api/antifraud/suspicious-ip", "/api/antifraud/stolencard").hasAuthority(Authority.SUPPORT.getAuthority());
                    auth.requestMatchers(HttpMethod.DELETE, "/api/antifraud/suspicious-ip/{ip}", "/api/antifraud/stolencard/{cardNumber}").hasAuthority(Authority.SUPPORT.getAuthority());
                    auth.anyRequest().denyAll();
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
