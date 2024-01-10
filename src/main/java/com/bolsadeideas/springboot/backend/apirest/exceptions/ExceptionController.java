package com.bolsadeideas.springboot.backend.apirest.exceptions;

import java.time.LocalDateTime;

import org.hibernate.validator.internal.IgnoreForbiddenApisErrors;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bolsadeideas.springboot.backend.apirest.dto.AccessDeniedExceptionDTO;
import com.bolsadeideas.springboot.backend.apirest.dto.BadCredentialsExceptionDTO;

import jakarta.servlet.http.HttpServletRequest;

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
	
	@ExceptionHandler(AuthenticationExceptionManaged.class)
	public ResponseEntity<String> returnCredentialsNotFoundException(AuthenticationExceptionManaged e){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
	
	@ExceptionHandler(InternalServerExceptionManaged.class)
	public ResponseEntity<String> returnInternalErrorException(InternalServerExceptionManaged e){
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> returnAccessDeniedException(HttpServletRequest request, AccessDeniedException ex){
		AccessDeniedExceptionDTO respuesta=new AccessDeniedExceptionDTO();
		respuesta.setMensajeBackend(ex.getLocalizedMessage());
		respuesta.setUrl(request.getRequestURL().toString());
		respuesta.setMethod(request.getMethod());
		respuesta.setMensaje("No posee permisos para acceder a esta función, por favor, contacta al administrador si crees que esto es un error");
		respuesta.setFecha(LocalDateTime.now());
		
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(respuesta);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<BadCredentialsExceptionDTO> returnBadCredentialsException(BadCredentialsException e){
		BadCredentialsExceptionDTO respuesta=new BadCredentialsExceptionDTO();
		respuesta.setMensajeBackend(e.getLocalizedMessage());
		respuesta.setMensaje("Credenciales no válidas");
		respuesta.setFecha(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
		
	}
}
