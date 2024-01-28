package com.springboot.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.data.PersonVO;
import com.springboot.services.PersonService;

@RestController
@RequestMapping("/person")
public class PersonController {

	private Logger logger = LoggerFactory.getLogger(PersonService.class);

	@Autowired
	private PersonService PersonService;

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public PersonVO findById(@PathVariable("id") Long id) {
		logger.info("Finding one PersonVO!");
		return PersonService.findById(id);
	}

	// INFO MediaType é necessário para o Swagger funcionar corretamente
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<PersonVO> findAll() {
		logger.info("Finding all PersonVOs!");
		return PersonService.findAll();
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public PersonVO create(@RequestBody PersonVO PersonVO) {
		logger.info("Creating a PersonVO!");
		return PersonService.create(PersonVO);
	}

	@PutMapping(value = "/{id}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public PersonVO update(@RequestBody PersonVO PersonVO) {
		logger.info("Updating a PersonVO!");
		return PersonService.update(PersonVO);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long id){
		logger.info("Deleting a PersonVO!");
		PersonService.delete(id);
		return ResponseEntity.noContent().build();	
	}

}