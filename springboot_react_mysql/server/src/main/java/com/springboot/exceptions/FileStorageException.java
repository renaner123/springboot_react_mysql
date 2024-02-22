package com.springboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FileStorageException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public FileStorageException(String ex, Throwable cause) {
		super(ex, cause);
	}

    public FileStorageException(String string) {
        //TODO Auto-generated constructor stub
    }
}