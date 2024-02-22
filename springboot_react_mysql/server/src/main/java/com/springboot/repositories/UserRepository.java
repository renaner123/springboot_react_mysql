package com.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.models.Person;
import com.springboot.models.User;

public interface UserRepository extends JpaRepository<Person, Long> {

    //JPA Query
    //INFO Caso seja necessário criar um método customizado, é possível criar utilizando query
    //Objeto User
    @Query("SELECT u FROM User u WHERE u.userName =:userName")
    User findByUsername(@Param("userName") String userName);
}