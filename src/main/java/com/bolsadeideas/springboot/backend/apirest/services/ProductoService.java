package com.bolsadeideas.springboot.backend.apirest.services;

import org.springframework.http.ResponseEntity;

public interface ProductoService {

	
	ResponseEntity<?> listarNombreDeProductos(String termino);
}
