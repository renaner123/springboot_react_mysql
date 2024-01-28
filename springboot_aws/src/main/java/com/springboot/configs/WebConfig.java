package com.springboot.configs;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    // https://baeldung.com/spring-mvc-content-negotiation-json-xml
    // Via EXTENSION. http://localhost:8080/api/person/v1.xml DEPRECATED
    // Via Query Parameter. htpp://localhost:8080/api/person/v1?mediaType=xml
    
    configurer.favorParameter(true)
        .parameterName("mediaType").ignoreAcceptHeader(false)
        .useRegisteredExtensionsOnly(false)
        .defaultContentType(MediaType.APPLICATION_JSON)
            .mediaType("jsonn", MediaType.APPLICATION_JSON)
            .mediaType("xml", MediaType.APPLICATION_XML);
    }

    

    
}
