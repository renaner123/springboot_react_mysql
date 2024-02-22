package com.springboot.unittests.mappers.mocks;

import java.util.ArrayList;
import java.util.List;

import com.springboot.data.vo.BookVO;
import com.springboot.models.Book;

public class MockBook {


    public Book mockEntity() {
        return mockEntity(0);
    }
    
    public BookVO mockVO() {
        return mockVO(0);
    }
    
    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<Book>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookVO> mockVOList() {
        List<BookVO> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockVO(i));
        }
        return books;
    }
    
    public Book mockEntity(Integer number) {
        Book book = new Book();
        book.setAuthor("Author Test" + number);
        book.setTitle("Title Test" + number);
        book.setLaunchDate(new java.util.Date());
        book.setId(number.longValue());
        book.setPrice((number) * 10.00);
        return book;
    }

    public BookVO mockVO(Integer number) {
        BookVO bookVo = new BookVO();
        bookVo.setAuthor("Author Test" + number);
        bookVo.setTitle("Title Test" + number);
        bookVo.setLaunchDate(new java.util.Date());
        bookVo.setKey(number.longValue());
        bookVo.setPrice((number + 1) * 10.00);
        return bookVo;
    }

}
