package com.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.models.Book;

public interface BookRepository extends JpaRepository<Book, Long>{}
