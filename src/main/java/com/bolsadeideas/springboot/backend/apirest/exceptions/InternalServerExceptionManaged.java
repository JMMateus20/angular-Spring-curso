package com.bolsadeideas.springboot.backend.apirest.exceptions;

public class InternalServerExceptionManaged extends RuntimeException{
	
	
	public InternalServerExceptionManaged(String mensaje) {
		super(mensaje);
	}

}
