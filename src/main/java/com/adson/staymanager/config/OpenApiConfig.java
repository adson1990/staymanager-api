package com.adson.staymanager.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI stayManagerOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("StayManager API")
                        .description("API para gestão de hotelaria, reservas, quartos, hóspedes e autenticação.")
                        .version("v1")
                        .contact(new Contact()
                                .name("Adson Farias")
                                .email("adsonalsf@gmail.com"))
                        .license(new License()
                                .name("Uso educacional/portfólio"))
                ) // encerrando info
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName)
                ) // Encerrando security item
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT"))
                ) // Encerrando components
                .externalDocs(new ExternalDocumentation()
                        .description("Repositório do projeto")
                        .url("https://github.com/adson1990/staymanager-api")
                ); // Encerrando externalDocs
    }
}