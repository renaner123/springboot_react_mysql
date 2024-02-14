package com.springboot.Integrationtests.controllers.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
		assertNotNull(createdPerson.getFirst_name());
		assertNotNull(createdPerson.getLast_name());
		assertNotNull(createdPerson.getAddress());
		assertNotNull(createdPerson.getGender());
		assertTrue(createdPerson.getEnabled());
		
		assertTrue(createdPerson.getId() > 0);

		assertEquals("Renan", createdPerson.getFirst_name());
		assertEquals("Rodolfo",createdPerson.getLast_name());
		assertEquals("Address", createdPerson.getAddress());
		assertEquals("Male", createdPerson.getGender());
	}
	
	@Test
	@Order(2)
	public void testUpdate() throws JsonMappingException, JsonProcessingException{
		person.setLast_name("Rodolfo2");
		
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
		assertNotNull(createdPerson.getFirst_name());
		assertNotNull(createdPerson.getLast_name());
		assertNotNull(createdPerson.getAddress());
		assertNotNull(createdPerson.getGender());
		assertTrue(createdPerson.getEnabled());
		
		assertEquals(person.getId(), createdPerson.getId());

		assertEquals("Renan", createdPerson.getFirst_name());
		assertEquals("Rodolfo2",createdPerson.getLast_name());
		assertEquals("Address", createdPerson.getAddress());
		assertEquals("Male", createdPerson.getGender());
	}


	private void mockPerson() {
		//precisa do contexto do spring para funcionar, por isso não fica no setup()
		person.setFirst_name("Renan");
		person.setLast_name("Rodolfo");
		person.setAddress("Address");
		person.setGender("Male");
		person.setEnabled(true);
	}
	
	@Test
	@Order(3)
	public void testDisablePersonById() throws JsonMappingException, JsonProcessingException{

		var content = 
			given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_RIGHT)
					.pathParam("id", person.getId())
					.when()
						.patch("{id}")
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
		assertFalse(persistedPerson.getEnabled());

		assertEquals(person.getId(), persistedPerson.getId());

		assertEquals("Renan", persistedPerson.getFirst_name());
		assertEquals("Rodolfo2",persistedPerson.getLast_name());
		assertEquals("Address", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
	}

	@Test
	@Order(4)
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
		assertFalse(persistedPerson.getEnabled());

		assertTrue(persistedPerson.getId() > 0);

		assertEquals("Renan", persistedPerson.getFirst_name());
		assertEquals("Rodolfo2",persistedPerson.getLast_name());
		assertEquals("Address", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
	}	
	@Test
	@Order(5)
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
	@Order(6)
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
		assertNotNull(foundPersonOne.getFirst_name());
		assertNotNull(foundPersonOne.getLast_name());
		assertNotNull(foundPersonOne.getAddress());
		assertNotNull(foundPersonOne.getGender());
		
		assertEquals(Long.valueOf(1), foundPersonOne.getId());
		
		assertEquals("Ayrton", foundPersonOne.getFirst_name());
		assertEquals("Senna", foundPersonOne.getLast_name());
		assertEquals("São Paulo", foundPersonOne.getAddress());
		assertEquals("Male", foundPersonOne.getGender());
		assertTrue(foundPersonOne.getEnabled());
		
		PersonVO foundPersonSix = people.get(5);
		
		assertNotNull(foundPersonSix.getId());
		assertNotNull(foundPersonSix.getFirst_name());
		assertNotNull(foundPersonSix.getLast_name());
		assertNotNull(foundPersonSix.getAddress());
		assertNotNull(foundPersonSix.getGender());
		assertTrue(foundPersonSix.getEnabled());
		
		assertEquals(Long.valueOf(9), foundPersonSix.getId());
		
		assertEquals("Nelson", foundPersonSix.getFirst_name());
		assertEquals("Mvezo", foundPersonSix.getLast_name());
		assertEquals("Mvezo – South Africa", foundPersonSix.getAddress());
		assertEquals("Male", foundPersonSix.getGender());
	}
	
	@Test
	@Order(7)
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
