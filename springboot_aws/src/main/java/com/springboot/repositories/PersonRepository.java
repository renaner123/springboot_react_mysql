package com.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.models.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {}