package com.samoilov.project.antifraud.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = OpenApiConfig.TITLE,
                description = OpenApiConfig.API_DESCRIPTION,
                version = OpenApiConfig.VERSION,
                license = @License(
                        name = OpenApiConfig.CREATOR_NAME,
                        url = OpenApiConfig.CONTACT_URL
                ),
                contact = @Contact(
                        name = OpenApiConfig.CREATOR_NAME,
                        email = OpenApiConfig.CONTACT_EMAIL,
                        url = OpenApiConfig.CONTACT_URL
                )
        ),
        servers = @Server(
                description = OpenApiConfig.LOCAL_ENVIRONMENT,
                url = OpenApiConfig.LOCALHOST_URL
        ),
        security = @SecurityRequirement(
                name = OpenApiConfig.BASIC_AUTH
        )
)
@SecurityScheme(
        name = OpenApiConfig.BASIC_AUTH,
        description = OpenApiConfig.SECURITY_DESCRIPTION,
        scheme = OpenApiConfig.SCHEME,
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

    public static final String TITLE = "Antifraud system api";

    public static final String API_DESCRIPTION = """
            This is OpenApi documentation for Antifraud system API with Spring Security basic authentication.
            @Created by SamoilovDev.
            """;

    public static final String VERSION = "1.0";

    public static final String CREATOR_NAME = "SamoilovDev";

    public static final String CONTACT_EMAIL = "vladimir.samoilov.dev@gmail.com";

    public static final String CONTACT_URL = "https://t.me/samoilov_vl";

    public static final String LOCAL_ENVIRONMENT = "Local environment";

    public static final String LOCALHOST_URL = "http://localhost:8080";

    public static final String BASIC_AUTH = "basicAuth";

    public static final String SECURITY_DESCRIPTION = "Basic authentication";

    public static final String SCHEME = "basic";

}