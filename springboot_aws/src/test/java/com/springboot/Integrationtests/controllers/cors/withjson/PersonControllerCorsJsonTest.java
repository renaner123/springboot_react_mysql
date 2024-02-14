package com.springboot.Integrationtests.controllers.cors.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.Integrationtests.VO.AccountCredentialsVO;
import com.springboot.Integrationtests.VO.PersonVO;
import com.springboot.Integrationtests.VO.TokenVO;
import com.springboot.Integrationtests.testcontainers.AbstractIntegrationTest;
import com.springboot.configs.TestConfigs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerCorsJsonTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	private static PersonVO person;
	
	@BeforeAll
	public static void setup() {
		specification = given().port(TestConfigs.SERVER_PORT);
		objectMapper = new ObjectMapper();
		//links do hateoaos não são reconhecidos, por isso precisa dessa propriedade desabilitada
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		person = new PersonVO();
	}
	
	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException{
		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");
		var tokenVO = 
				given()
				    .basePath("/auth/signin")
				    	.port(TestConfigs.SERVER_PORT)
				    	.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.body(user)
						.when()
					.post()
						.then()
							.statusCode(200)
								.extract()
								.body()
									.as(TokenVO.class)
								.getAccessToken();
								
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO)
				.setBasePath("api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}
	@Test
	@Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException{
		mockPerson();
		
		var content = 
			given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_RIGHT)
					.body(person)
					.when()
						.post()
				.then()
					.statusCode(200)
				.extract()
					.body()
						.asString();

		PersonVO createdPerson = objectMapper.readValue(content, PersonVO.class);
		person = createdPerson;

		assertNotNull(createdPerson);
		assertNotNull(createdPerson.getId());
		assertNotNull(createdPerson.getFirst_name());
		assertNotNull(createdPerson.getLast_name());
		assertNotNull(createdPerson.getAddress());
		assertNotNull(createdPerson.getGender());

		assertTrue(createdPerson.getId() > 0);

		assertEquals("First Name", createdPerson.getFirst_name());
		assertEquals("Last Name",createdPerson.getLast_name());
		assertEquals("Address", createdPerson.getAddress());
		assertEquals("Male", createdPerson.getGender());
	}

	@Test
	@Order(2)
	public void testCreateWithWrongOrigin() throws JsonMappingException, JsonProcessingException{
		mockPerson();
		
		var content = 
			given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_WRONG)
					.body(person)
					.when()
						.post()
				.then()
					.statusCode(403)
				.extract()
					.body()
						.asString();

		assertNotNull(content);
		assertEquals("Invalid CORS request", content);

	}

	private void mockPerson() {
		//precisa do contexto do spring para funcionar, por isso não fica no setup()
		person.setFirst_name("First Name");
		person.setLast_name("Last Name");
		person.setAddress("Address");
		person.setGender("Male");
		person.setEnabled(true);
	}

	@Test
	@Order(3)
	public void testFindById() throws JsonMappingException, JsonProcessingException{
		mockPerson();

		var content = 
			given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_RIGHT)
					.pathParam("id", person.getId())
					.when()
						.get("{id}")
				.then()
					.statusCode(200)
				.extract()
					.body()
						.asString();

		PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
		person = persistedPerson;

		assertNotNull(persistedPerson);
		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirst_name());
		assertNotNull(persistedPerson.getLast_name());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());

		assertTrue(persistedPerson.getId() > 0);

		assertEquals("First Name", persistedPerson.getFirst_name());
		assertEquals("Last Name",persistedPerson.getLast_name());
		assertEquals("Address", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
	}

	@Test
	@Order(4)
	public void testFindByIdWithWrongOrigin() throws JsonMappingException, JsonProcessingException{
		mockPerson();

		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_WRONG)
				.setBasePath("api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		var content = 
			given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.pathParam("id", person.getId())
					.when()
						.get("{id}")
				.then()
					.statusCode(403)
				.extract()
					.body()
						.asString();

		assertNotNull(content);
		assertEquals("Invalid CORS request", content);
	}

}
