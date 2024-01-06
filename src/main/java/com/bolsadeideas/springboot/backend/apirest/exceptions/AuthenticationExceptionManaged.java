package com.bolsadeideas.springboot.backend.apirest.exceptions;

public class AuthenticationExceptionManaged extends RuntimeException{
	
	public AuthenticationExceptionManaged(String mensaje) {
		super(mensaje);
	}

}
