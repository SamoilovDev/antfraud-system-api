package com.samoilov.project.antifraud.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.text.SimpleDateFormat;
import java.util.List;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(List.of(new BasicAuth("basicAuth")))
                .securityContexts(
                        List.of(
                                SecurityContext
                                        .builder()
                                        .securityReferences(
                                                List.of(
                                                        SecurityReference
                                                                .builder()
                                                                .reference("basicAuth")
                                                                .scopes(new AuthorizationScope[0])
                                                                .build()
                                                )
                                        )
                                        .build()
                        )
                );
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"))
                .setDefaultLeniency(false)
                .enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder(15);
    }

}