package com.bolsadeideas.springboot.backend.apirest.exceptions;

import org.springframework.dao.DataAccessException;

public class DataAccessExceptionManaged extends DataAccessException{

	public DataAccessExceptionManaged(String msg) {
		super(msg);
		
	}
	
	

}
