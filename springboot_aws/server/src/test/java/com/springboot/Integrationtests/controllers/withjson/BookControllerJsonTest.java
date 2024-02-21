package com.springboot.Integrationtests.controllers.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

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
import com.springboot.Integrationtests.VO.BookVO;
import com.springboot.Integrationtests.VO.TokenVO;
import com.springboot.Integrationtests.VO.wrappers.WrapperBookVO;
import com.springboot.Integrationtests.testcontainers.AbstractIntegrationTest;
import com.springboot.configs.TestConfigs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class BookControllerJsonTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	private static BookVO book;
	
	@BeforeAll
	public static void setup() {
		specification = given().port(TestConfigs.SERVER_PORT);
		objectMapper = new ObjectMapper();

		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		book = new BookVO();
	}
	
	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException{
		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");
		var accessToken = 
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
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.setBasePath("api/book/v1")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}
	
	@Test
	@Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException{
		mockBook();
		
		var content = 
			given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.body(book)
					.when()
						.post()
				.then()
					.statusCode(200)
				.extract()
					.body()
						.asString();

		BookVO createdBook = objectMapper.readValue(content, BookVO.class);
		book = createdBook;

		assertNotNull(createdBook);
		assertNotNull(createdBook.getKey());
		assertNotNull(createdBook.getAuthor());
		assertNotNull(createdBook.getTitle());
		assertNotNull(createdBook.getPrice());
		assertNotNull(createdBook.getLaunchDate());

		assertTrue(createdBook.getKey() > 0);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(createdBook.getLaunchDate());

		assertEquals("Renan", createdBook.getAuthor());
		assertEquals("Teste",createdBook.getTitle());
		assertEquals(Double.valueOf(10), createdBook.getPrice());
		assertEquals(2024, calendar.get(Calendar.YEAR));
		assertEquals(2, calendar.get(Calendar.MONTH));
		assertEquals(12, calendar.get(Calendar.DAY_OF_MONTH));
	}
	
	 @Test
	 @Order(2)
	 public void testUpdate() throws JsonMappingException, JsonProcessingException{
	 	book.setAuthor("Rodolfo2");
		
	 	var content = 
	 		given().spec(specification)
	 			.contentType(TestConfigs.CONTENT_TYPE_JSON)
	 				.body(book)
	 				.when()
	 					.post()
	 			.then()
	 				.statusCode(200)
	 			.extract()
	 				.body()
	 					.asString();
		
	 	BookVO createdBook = objectMapper.readValue(content, BookVO.class);
	 	book = createdBook;

		assertNotNull(createdBook);
		assertNotNull(createdBook.getKey());
		assertNotNull(createdBook.getAuthor());
		assertNotNull(createdBook.getTitle());
		assertNotNull(createdBook.getPrice());
		assertNotNull(createdBook.getLaunchDate());

		assertEquals(book.getKey(), createdBook.getKey());
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(createdBook.getLaunchDate());

		assertEquals("Rodolfo2", createdBook.getAuthor());
		assertEquals("Teste",createdBook.getTitle());
		assertEquals(Double.valueOf(10), createdBook.getPrice());
		assertEquals(2024, calendar.get(Calendar.YEAR));
		assertEquals(2, calendar.get(Calendar.MONTH));
		assertEquals(12, calendar.get(Calendar.DAY_OF_MONTH));
	 }


	private void mockBook() {
		book.setAuthor("Renan");
		book.setTitle("Teste");
		book.setPrice(10.0);
		Calendar launchDate = Calendar.getInstance();
		launchDate.set(2024, 2, 12);
		book.setLaunchDate(launchDate.getTime());
	}

	 @Test
	 @Order(3)
	 public void testFindById() throws JsonMappingException, JsonProcessingException{
	 	mockBook();

	 	var content = 
	 		given().spec(specification)
	 			.contentType(TestConfigs.CONTENT_TYPE_JSON)
	 				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_RIGHT)
	 				.pathParam("id", book.getKey())
	 				.when()
	 					.get("{id}")
	 			.then()
	 				.statusCode(200)
	 			.extract()
	 				.body()
	 					.asString();

	 	BookVO persistedbook = objectMapper.readValue(content, BookVO.class);
	 	book = persistedbook;

		assertNotNull(persistedbook);
		assertNotNull(persistedbook.getKey());
		assertNotNull(persistedbook.getAuthor());
		assertNotNull(persistedbook.getTitle());
		assertNotNull(persistedbook.getPrice());
		assertNotNull(persistedbook.getLaunchDate());

		assertTrue(persistedbook.getKey() > 0);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(persistedbook.getLaunchDate());

		assertEquals("Rodolfo2", persistedbook.getAuthor());
		assertEquals("Teste",persistedbook.getTitle());
		assertEquals(Double.valueOf(10), persistedbook.getPrice());
		assertEquals(2024, calendar.get(Calendar.YEAR));
		assertEquals(2, calendar.get(Calendar.MONTH));
		assertEquals(12, calendar.get(Calendar.DAY_OF_MONTH));
	 }

	 @Test
	 @Order(4)
	 public void testDelete() throws JsonMappingException, JsonProcessingException{

	 	given().spec(specification)
	 		.contentType(TestConfigs.CONTENT_TYPE_JSON)
	 			.pathParam("id", book.getKey())
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
				.queryParams("page", 0, "size", 10, "direction", "asc")
	 				.when()
	 				.get()
	 			.then()
	 				.statusCode(200)
	 					.extract()
	 					.body()
	 						.asString();
		
		WrapperBookVO wrapper = objectMapper.readValue(content, WrapperBookVO.class);
		var book = wrapper.getEmbedded().getBooks();
		
	 	BookVO foundbookOne = book.get(0);
	 	
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(foundbookOne.getLaunchDate());
		
		assertNotNull(foundbookOne);
		assertNotNull(foundbookOne.getKey());
		assertNotNull(foundbookOne.getAuthor());
		assertNotNull(foundbookOne.getTitle());
		assertNotNull(foundbookOne.getPrice());
		assertNotNull(foundbookOne.getLaunchDate());		

		assertEquals(foundbookOne.getKey(),Long.valueOf(12));
		
		assertEquals("Viktor Mayer-Schonberger e Kenneth Kukier", foundbookOne.getAuthor());
		assertEquals("Big Data: como extrair volume, variedade, velocidade e valor da avalanche de informação cotidiana",foundbookOne.getTitle());
		assertEquals(Double.valueOf(54), foundbookOne.getPrice());
		assertEquals(07, calendar.get(Calendar.DAY_OF_MONTH));
		assertEquals(10, calendar.get(Calendar.MONTH));
		assertEquals(2017, calendar.get(Calendar.YEAR));
		
	 	BookVO foundbookSix = book.get(5);
	 	
		calendar.setTime(foundbookSix.getLaunchDate());
				
		assertNotNull(foundbookSix);
		assertNotNull(foundbookSix.getKey());
		assertNotNull(foundbookSix.getAuthor());
		assertNotNull(foundbookSix.getTitle());
		assertNotNull(foundbookSix.getPrice());
		assertNotNull(foundbookSix.getLaunchDate());

		assertEquals(foundbookSix.getKey(),Long.valueOf(11));		

		assertEquals("Roger S. Pressman", foundbookSix.getAuthor());
		assertEquals("Engenharia de Software: uma abordagem profissional",foundbookSix.getTitle());
		assertEquals(Double.valueOf(56), foundbookSix.getPrice());
		assertEquals(07, calendar.get(Calendar.DAY_OF_MONTH));
		assertEquals(10, calendar.get(Calendar.MONTH));
		assertEquals(2017, calendar.get(Calendar.YEAR));

	 }
	
	 @Test
	 @Order(6)
	 public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {
		
	 	RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
	 		.setBasePath("/api/book/v1")
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
