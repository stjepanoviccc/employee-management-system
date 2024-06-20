package com.example.ems_app.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi createApi() {
        return GroupedOpenApi.builder()
                .group("public-api")
                .pathsToMatch("/**")
                .build();
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Employee Management System API")
                        .description("API documentation for managing employees within the organization.\n\n" +
                                "### Initialize\n\n" +
                                "If this is your first time using this API, you have the option to initialize data to populate the database with several employees.\n\n" +
                                "### Authentication\n\n" +
                                "Before accessing employee-related endpoints, you need to register and authenticate.\n\n" +
                                "### Roles\n\n" +
                                "Supported roles: USER and ADMIN.\n\n" +
                                "- **Unauthenticated**: Users can login and register.\n" +
                                "- **USER**: Can retrieve all employees and get employee details by ID.\n" +
                                "- **ADMIN**: Has full access to CRUD operations on employees and advanced queries.\n\n" +
                                "### Employees\n\n" +
                                "Endpoints for CRUD operations on employees, as well as advanced queries.\n" +
                                "Endpoints are protected and require appropriate role-based access.\n\n" +
                                "### Queries\n\n" +
                                "- Search employees by criteria such as first name, last name, email, position, salary range and department.\n" +
                                "- Retrieve employees with the highest and lowest salaries.\n" +
                                "- Filter employees by position and salary range.\n" +
                                "- Create, update, or delete employees.\n\n" +
                                "- etc ...\n\n" +
                                "Endpoints are secured using JWT authentication.\n")
                        .version("1.0"))
                .addSecurityItem(new SecurityRequirement().
                        addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes
                        ("Bearer Authentication", createAPIKeyScheme()))
                .tags(Arrays.asList(
                        new Tag().name("init").description("Initialize data if employees table is empty"),
                        new Tag().name("auth").description("Operations related to authentication"),
                        new Tag().name("employees").description("Operations related to employees")
                ));
    }

}
