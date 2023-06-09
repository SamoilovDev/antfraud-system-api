package com.samoilov.project.antifraud.config;

import com.samoilov.project.antifraud.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
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
                .httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint) // Handles auth error
                .and()
                .csrf().disable().headers().frameOptions().disable() // for Postman, the H2 console
                .and()
                .authorizeRequests() // manage access
                .antMatchers("/actuator/shutdown").permitAll() // needs to run tests
                .antMatchers(HttpMethod.POST, "/api/auth/user").permitAll() // anyone can create a user
                .antMatchers(HttpMethod.GET, "/api/auth/list").authenticated() // only authenticated users can list users
                .antMatchers(HttpMethod.DELETE, "/api/auth/user/{username}").authenticated() // only authenticated users can delete users
                .antMatchers(HttpMethod.POST, "/api/antifraud/transaction").authenticated() // only authenticated users can create transactions
                .anyRequest().denyAll() // deny all other requests
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().build(); // no session
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(encoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }


}
