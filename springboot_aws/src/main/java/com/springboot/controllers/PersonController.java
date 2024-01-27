package com.springboot.controllers;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.models.Person;
import com.springboot.services.PersonService;


@RestController
public class PersonController {
	
	private final AtomicLong counter = new AtomicLong();

	@Autowired
	private PersonService personService;
	
	@RequestMapping(value="/person/{id}", 
			method=RequestMethod.GET,
			produces = "application/json")
	public Person person(@PathVariable("id") String id) {
		return personService.findById(id);
	}
}