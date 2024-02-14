package com.springboot.Integrationtests.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.springboot.Integrationtests.testcontainers.AbstractIntegrationTest;
import com.springboot.models.Person;
import com.springboot.repositories.PersonRepository;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(OrderAnnotation.class)
public class PersonRepositoryTest extends AbstractIntegrationTest{
	
	//INFO apenas exemplo para ser utilizado em necessidades futuras
	@Autowired
	PersonRepository personRepository;
	
	private static Person person;
	
	@BeforeAll
	public static void setup() {
		person = new Person();
	}
	
	@Test
	@Order(0)
	public void testFindByName() throws JsonMappingException, JsonProcessingException {
		
		Pageable pageable = PageRequest.of(0, 6, Sort.by(Direction.ASC, "firstName"));
		person = personRepository.findPersonsByName("ayr", pageable).getContent().get(0);

		
		assertNotNull(person.getId());
		
		assertNotNull(person.getFirstName());
		assertNotNull(person.getLastName());
		assertNotNull(person.getAddress());
		assertNotNull(person.getGender());
		assertTrue(person.getEnabled());
		
		assertEquals(Long.valueOf(1), person.getId());
		
		assertEquals("Ayrton", person.getFirstName());
		assertEquals("Senna", person.getLastName());
		assertEquals("São Paulo", person.getAddress());
		assertEquals("Male", person.getGender());
		assertTrue(person.getEnabled());	
	}
	
	@Test
	@Order(1)
	public void testDisablePerson() throws JsonMappingException, JsonProcessingException {
		
		personRepository.disablePerson(person.getId());
		
		Pageable pageable = PageRequest.of(0, 6, Sort.by(Direction.ASC, "firstName"));
		person = personRepository.findPersonsByName("ayr", pageable).getContent().get(0);

		
		assertNotNull(person.getId());
		
		assertNotNull(person.getFirstName());
		assertNotNull(person.getLastName());
		assertNotNull(person.getAddress());
		assertNotNull(person.getGender());
		assertFalse(person.getEnabled());
		
		assertEquals(Long.valueOf(1), person.getId());
		
		assertEquals("Ayrton", person.getFirstName());
		assertEquals("Senna", person.getLastName());
		assertEquals("São Paulo", person.getAddress());
		assertEquals("Male", person.getGender());
	}
}
