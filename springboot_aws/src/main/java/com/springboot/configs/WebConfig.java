package com.springboot.configs;


import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.springboot.Serialization.Converter.YamlJackson2HttpMessageConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer{

    private static final MediaType MEDIA_TYPE_YML = MediaType.valueOf("application/x-yaml");

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    // https://baeldung.com/spring-mvc-content-negotiation-json-xml
    // Via EXTENSION. http://localhost:8080/api/person/v1.xml DEPRECATED

    // Via Query Parameter. htpp://localhost:8080/api/person/v1?mediaType=xml
    
    // configurer.favorParameter(true)
    //     .parameterName("mediaType").ignoreAcceptHeader(false)
    //     .useRegisteredExtensionsOnly(false)
    //     .defaultContentType(MediaType.APPLICATION_JSON)
    //         .mediaType("jsonn", MediaType.APPLICATION_JSON)
    //         .mediaType("xml", MediaType.APPLICATION_XML);
    // }

    // Via Header param. http://localhost:8080/api/person/v1

    configurer.favorParameter(false)
        .ignoreAcceptHeader(false)
        .useRegisteredExtensionsOnly(false)
        .defaultContentType(MediaType.APPLICATION_JSON)
            .mediaType("jsonn", MediaType.APPLICATION_JSON)
            .mediaType("xml", MediaType.APPLICATION_XML)
            .mediaType("yaml", MEDIA_TYPE_YML);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new YamlJackson2HttpMessageConverter());
    }    
    
}
