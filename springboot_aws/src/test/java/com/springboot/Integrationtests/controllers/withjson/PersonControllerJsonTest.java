package com.springboot.Integrationtests.controllers.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
public class PersonControllerJsonTest extends AbstractIntegrationTest {

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
		assertNotNull(createdPerson.getFirstName());
		assertNotNull(createdPerson.getLastName());
		assertNotNull(createdPerson.getAddress());
		assertNotNull(createdPerson.getGender());

		assertTrue(createdPerson.getId() > 0);

		assertEquals("Renan", createdPerson.getFirstName());
		assertEquals("Rodolfo",createdPerson.getLastName());
		assertEquals("Address", createdPerson.getAddress());
		assertEquals("Male", createdPerson.getGender());
	}
	
	@Test
	@Order(2)
	public void testUpdate() throws JsonMappingException, JsonProcessingException{
		person.setLastName("Rodolfo2");
		
		var content = 
			given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.body(person)
					.when()
						.post()
				.then()
					.statusCode(200)
				.extract()
					.body()
						.asString();
		
		//controla como vai ser serializado, pois não tem supressão de warning
		PersonVO createdPerson = objectMapper.readValue(content, PersonVO.class);
		person = createdPerson;

		assertNotNull(createdPerson);
		assertNotNull(createdPerson.getId());
		assertNotNull(createdPerson.getFirstName());
		assertNotNull(createdPerson.getLastName());
		assertNotNull(createdPerson.getAddress());
		assertNotNull(createdPerson.getGender());

		assertEquals(person.getId(), createdPerson.getId());

		assertEquals("Renan", createdPerson.getFirstName());
		assertEquals("Rodolfo2",createdPerson.getLastName());
		assertEquals("Address", createdPerson.getAddress());
		assertEquals("Male", createdPerson.getGender());
	}


	private void mockPerson() {
		//precisa do contexto do spring para funcionar, por isso não fica no setup()
		person.setFirstName("Renan");
		person.setLastName("Rodolfo");
		person.setAddress("Address");
		person.setGender("Male");
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
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());

		assertTrue(persistedPerson.getId() > 0);

		assertEquals("Renan", persistedPerson.getFirstName());
		assertEquals("Rodolfo2",persistedPerson.getLastName());
		assertEquals("Address", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
	}

	@Test
	@Order(4)
	public void testDelete() throws JsonMappingException, JsonProcessingException{

		given().spec(specification)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.pathParam("id", person.getId())
				.when()
				.delete("{id}")
			.then()
				.statusCode(204);
	}

	@Test
	@Order(5)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.when()
					.get()
				.then()
					.statusCode(200)
						.extract()
						.body()
							.asString();
		
		List<PersonVO> people = objectMapper.readValue(content, new TypeReference<List<PersonVO>>() {});
		
		PersonVO foundPersonOne = people.get(0);
		
		assertNotNull(foundPersonOne.getId());
		assertNotNull(foundPersonOne.getFirstName());
		assertNotNull(foundPersonOne.getLastName());
		assertNotNull(foundPersonOne.getAddress());
		assertNotNull(foundPersonOne.getGender());
		
		//assertEquals(1, foundPersonOne.getId());
		
		assertEquals("Ayrton", foundPersonOne.getFirstName());
		assertEquals("Senna", foundPersonOne.getLastName());
		assertEquals("São Paulo", foundPersonOne.getAddress());
		assertEquals("Male", foundPersonOne.getGender());
		
		PersonVO foundPersonSix = people.get(5);
		
		assertNotNull(foundPersonSix.getId());
		assertNotNull(foundPersonSix.getFirstName());
		assertNotNull(foundPersonSix.getLastName());
		assertNotNull(foundPersonSix.getAddress());
		assertNotNull(foundPersonSix.getGender());
		
		//assertEquals(9, foundPersonSix.getId());
		
		assertEquals("Nelson", foundPersonSix.getFirstName());
		assertEquals("Mvezo", foundPersonSix.getLastName());
		assertEquals("Mvezo – South Africa", foundPersonSix.getAddress());
		assertEquals("Male", foundPersonSix.getGender());
	}
	
	@Test
	@Order(6)
	public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {
		
		RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
			.setBasePath("/api/person/v1")
			.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
			.build();
		
		given().spec(specificationWithoutToken)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.when()
				.get()
			.then()
				.statusCode(403);
	}


}
