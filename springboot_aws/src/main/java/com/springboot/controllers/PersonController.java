package com.springboot.controllers;

import java.util.List;

import javax.print.attribute.standard.Media;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.data.v1.PersonVO;
import com.springboot.data.v2.PersonVOV2;
import com.springboot.services.PersonService;
import com.springboot.utils.MediaType;

@RestController
@RequestMapping("/api/person/v1")
public class PersonController {

	private Logger logger = LoggerFactory.getLogger(PersonService.class);

	@Autowired
	private PersonService PersonService;

	@GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON,
		 	MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML })
	public PersonVO findById(@PathVariable("id") Long id) {
		logger.info("Finding one PersonVO!");
		return PersonService.findById(id);
	}

	// INFO MediaType é necessário para o Swagger funcionar corretamente
	@GetMapping(produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	public List<PersonVO> findAll() {
		logger.info("Finding all PersonVOs!");
		return PersonService.findAll();
	}

	@PostMapping(consumes = { MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML, MediaType.APPLICATION_YML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	public PersonVO create(@RequestBody PersonVO PersonVO) {
		logger.info("Creating a PersonVO!");
		return PersonService.create(PersonVO);
	}

	// surgiu a "necessidade" de adicionar mais um campo, para não quebrar os
	// clientes que já utilizam, uma possibilidade é adicionar uma nova rota.
	@PostMapping(value = "/v2", consumes = { MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML, MediaType.APPLICATION_YML }, produces = { MediaType.APPLICATION_JSON,
					MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	public PersonVOV2 createV2(@RequestBody PersonVOV2 PersonVOV2) {
		logger.info("Creating a PersonVO!");
		return PersonService.createV2(PersonVOV2);
	}

	@PutMapping(value = "/{id}", consumes = { MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML, MediaType.APPLICATION_YML }, produces = { MediaType.APPLICATION_JSON,
					MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	public PersonVO update(@RequestBody PersonVO PersonVO) {
		logger.info("Updating a PersonVO!");
		return PersonService.update(PersonVO);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
		logger.info("Deleting a PersonVO!");
		PersonService.delete(id);
		return ResponseEntity.noContent().build();
	}

}