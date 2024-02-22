package com.springboot.services;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import com.springboot.controllers.BookController;
import com.springboot.data.vo.BookVO;
import com.springboot.data.vo.PersonVO;
import com.springboot.exceptions.RequiredObjectsNullException;
import com.springboot.exceptions.ResourceNotFoundException;
import com.springboot.mappers.DozerMapper;
import com.springboot.models.Book;
import com.springboot.repositories.BookRepository;

@Service
public class BookServices {

    private Logger logger = Logger.getLogger(BookServices.class.getName());
    
    @Autowired
    private BookRepository repository;

    @Autowired
	PagedResourcesAssembler<BookVO> assembler;


	public PagedModel<EntityModel<BookVO>> findAll(Pageable pageable) {

        logger.info("Finding all books!");

		var bookPage = repository.findAll(pageable);
		var bookVosPage = bookPage.map(p -> DozerMapper.parseObject(p, BookVO.class));
		bookVosPage.stream()
				.forEach(p -> p.add(linkTo(methodOn(BookController.class).findById(p.getKey())).withSelfRel()));

		Link link = linkTo(methodOn(BookController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(),
                "asc")).withSelfRel();
		return assembler.toModel(bookVosPage, link);
    }

    public BookVO findById(Long id){
            
            logger.info("Finding one book!");
    
            var entity = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
            var vo = DozerMapper.parseObject(entity, BookVO.class);
            vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
            return vo;
    }

    public BookVO create(BookVO book){

        if(book == null) throw new RequiredObjectsNullException();

        logger.info("Creating one book!");

        var entity = DozerMapper.parseObject(book, Book.class);
        var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public BookVO update(BookVO book){

        if(book == null) throw new RequiredObjectsNullException();

        Book entity = repository.findById(book.getKey())
            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setAuthor(book.getAuthor());
        entity.setLaunchDate(book.getLaunchDate());
        entity.setPrice(book.getPrice());
        entity.setTitle(book.getTitle());

        var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        
        return vo;
    }

    public void delete(Long id){

        var entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        repository.delete(entity);

    }
}       
