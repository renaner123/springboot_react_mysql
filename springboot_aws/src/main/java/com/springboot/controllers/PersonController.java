package com.springboot.controllers;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.models.Person;
import com.springboot.services.PersonService;

@RestController
@RequestMapping("/person")
public class PersonController {

	private final AtomicLong counter = new AtomicLong();
	private Logger logger = LoggerFactory.getLogger(PersonService.class);

	@Autowired
	private PersonService personService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	public Person findById(@PathVariable("id") Long id) {
		logger.info("Finding one person!");
		return personService.findById(id);
	}

	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public List<Person> findAll() {
		logger.info("Finding all persons!");
		return personService.findall();
	}

	@RequestMapping(method = RequestMethod.POST, 
			consumes = "application/json",
			produces = "application/json")
	public Person create(@RequestBody Person person) {
		logger.info("Creating a person!");
		return personService.create(person);
	}

	@RequestMapping(value = "/{id}",
			method = RequestMethod.PUT, 
			consumes = "application/json",
			produces = "application/json")
	public Person update(@RequestBody Person person) {
		logger.info("Updating a person!");
		return personService.update(person);
	}

	@RequestMapping(value = "/{id}",
	method = RequestMethod.DELETE)
	public void delete(@PathVariable(value = "id") Long id){
		logger.info("Deleting a person!");
		personService.delete(id);
	}

}