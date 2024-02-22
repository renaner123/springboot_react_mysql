package com.springboot.Integrationtests.controllers.withyaml;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.springboot.Integrationtests.VO.AccountCredentialsVO;
import com.springboot.Integrationtests.VO.TokenVO;
import com.springboot.Integrationtests.controllers.withyaml.mapper.YMLMapper;
import com.springboot.Integrationtests.testcontainers.AbstractIntegrationTest;
import com.springboot.configs.TestConfigs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class AuthControllerYamlTest extends AbstractIntegrationTest{

    private static TokenVO tokenVO;
	private static YMLMapper mapper;

	@BeforeAll
	public static void setup(){
		mapper = new YMLMapper();
	}

	@Test
	@Order(1)
	public void testSignin() throws JsonMappingException, JsonProcessingException{
		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

		//INFO dar detalhes de log
		RequestSpecification specification = new RequestSpecBuilder()
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
			.build();
        
		tokenVO = 
				//INFO add .spec(specification) para exibir os detalhes
				given().spec(specification)
					.config(RestAssuredConfig.config()
					.encoderConfig(EncoderConfig.encoderConfig()
						.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				    .accept(TestConfigs.CONTENT_TYPE_YML)
					.basePath("/auth/signin")
				    	.port(TestConfigs.SERVER_PORT)
				    	.contentType(TestConfigs.CONTENT_TYPE_YML)
					.body(user, mapper)
						.when()
					.post()
						.then()
							.statusCode(200)
								.extract()
								.body()
									.as(TokenVO.class, mapper);

        assertNotNull(tokenVO.getAccessToken());
        assertNotNull(tokenVO.getRefreshToken());
        //poderia verificar se o token é válido, mas não é o objetivo desse teste.								
	}

	@Test
	@Order(2)
	public void testRefresh() throws JsonMappingException, JsonProcessingException{
        
		var newTokenVO = 
					given()
					.config(RestAssuredConfig.config()
					.encoderConfig(EncoderConfig.encoderConfig()
						.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
					.accept(TestConfigs.CONTENT_TYPE_YML)
					.basePath("/auth/signin")
						.port(TestConfigs.SERVER_PORT)
						.contentType(TestConfigs.CONTENT_TYPE_YML)
				    .basePath("/auth/refresh")
				    	.port(TestConfigs.SERVER_PORT)
				    	.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.pathParam("username", tokenVO.getUsername())
                    .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
					.when()
					    .put("{username}")
					.then()
						.statusCode(200)
					.extract()
						.body()
							.as(TokenVO.class, mapper);

        assertNotNull(newTokenVO.getAccessToken());
        assertNotNull(newTokenVO.getRefreshToken());
        //poderia verificar se o token é válido, mas não é o objetivo desse teste.								
	}    
}
