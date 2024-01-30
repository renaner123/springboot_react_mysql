package com.springboot.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {
    
    @Bean 
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components())
            .info(new Info()
                .title("RESTful API with Java 21 and Spring Boot")
                .description("Spring Boot AWS")
                .version("1.0.0")
                .termsOfService(null)
                .description("REST API's RESTFul do 0 à AWS c. Spring Boot 3 Java e Docker")
                .license(new License().name("Apache 2.0").url("http://springdoc.org"))
            );
    }
}
