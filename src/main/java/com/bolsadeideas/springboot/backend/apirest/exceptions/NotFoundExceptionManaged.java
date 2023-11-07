package com.bolsadeideas.springboot.backend.apirest.exceptions;

public class NotFoundExceptionManaged extends RuntimeException{
	
	public NotFoundExceptionManaged(String mensaje) {
		super(mensaje);
	}

}
