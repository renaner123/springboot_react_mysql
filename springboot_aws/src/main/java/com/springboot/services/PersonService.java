package com.springboot.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.exceptions.ResourceNotFoundExcpetion;
import com.springboot.models.Person;
import com.springboot.repositories.PersonRepository;

@Service
public class PersonService {
    
    private final AtomicLong counter = new AtomicLong();
    private Logger logger = LoggerFactory.getLogger(PersonService.class);

    @Autowired
    private PersonRepository personRepositoty;

    public Person findById(Long id){

        logger.info("Finding one person!");

        // "Mock"
        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("123 Main St");
        person.setGender("Male");


        return personRepositoty.findById(id).orElseThrow(() -> new ResourceNotFoundExcpetion("No records found for this ID"));
    }

    public List<Person> findall(){
        List<Person> persons = new ArrayList<>();

        logger.info("Finding all persons!");

        return personRepositoty.findAll();
    }

    public Person create(Person person){
        logger.info("Creating a person!");
        return personRepositoty.save(person);
    }

    public Person update(Person person){
        logger.info("Updating a person!");
        var entity = personRepositoty.findById(person.getId())
            .orElseThrow(() -> new ResourceNotFoundExcpetion("No records found for this ID"));
            
            entity.setFirstName(person.getFirstName());
            entity.setLastName(person.getLastName());
            entity.setAddress(person.getAddress());
            entity.setGender(person.getGender());

            return personRepositoty.save(entity);
    }

    public void delete(Long id){
        logger.info("Deleting a person!");
        var entity = personRepositoty.findById(id)
        .orElseThrow(() -> new ResourceNotFoundExcpetion("No records found for this ID"));
        
        personRepositoty.delete(entity);
    }
}
