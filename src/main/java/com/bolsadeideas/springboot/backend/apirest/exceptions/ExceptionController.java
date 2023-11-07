package com.bolsadeideas.springboot.backend.apirest.exceptions;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler(NotFoundExceptionManaged.class)
	public ResponseEntity<String> returnNotFoundException(NotFoundExceptionManaged e){
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(DataAccessExceptionManaged.class)
	public ResponseEntity<String> returnDataAccessException(DataAccessExceptionManaged e){
		return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
