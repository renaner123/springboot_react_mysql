package com.springboot.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.models.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
	
	@Modifying
    @Query("UPDATE Person p SET p.enabled = false WHERE p.id =:id")
    void disablePerson(@Param("id") Long id);
	
	// Retornar leandro caso ache %and%
	// LIKE pode não ser interessante se tiver muitos dados. Verificar com equipe de banco de dados.
	@Query("SELECT p FROM Person p WHERE p.firstName LIKE LOWER(CONCAT ('%',:firstName,'%'))")
	Page<Person> findPersonsByName(@Param("firstName") String firstName, Pageable pageable);
	
}