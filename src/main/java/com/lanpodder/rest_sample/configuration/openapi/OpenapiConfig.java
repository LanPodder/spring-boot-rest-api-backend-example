package com.lanpodder.rest_sample.configuration.openapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenapiConfig {
  @Bean
  OpenAPI openAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Rest API with spring boot")
            .description(
                "A Rest API secured with spring security, with example tests, example endpoints, services, mappers, dtos and minio for file uploads")
            .version("1.0.0"))
        .components(new Components()
            .addSecuritySchemes("bearerAuth", new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Keycloak access token")))
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
  }
}
