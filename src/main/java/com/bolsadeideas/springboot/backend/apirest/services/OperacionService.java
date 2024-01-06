package com.bolsadeideas.springboot.backend.apirest.services;

import org.springframework.http.ResponseEntity;

import com.bolsadeideas.springboot.backend.apirest.dto.RegistroOperacionDTO;

public interface OperacionService {
	
	
	ResponseEntity<String> eliminar(Long operacionId);
	
	
	ResponseEntity<?> actualizar(Long operacionId, RegistroOperacionDTO datos);

}
