package com.bolsadeideas.springboot.backend.apirest.services;

import org.springframework.http.ResponseEntity;

import com.bolsadeideas.springboot.backend.apirest.dto.RegistroRegionDTO;

public interface RegionService {
	
	ResponseEntity<?> insertar(RegistroRegionDTO datos);
	
	
	ResponseEntity<?> listar();
	
	
	ResponseEntity<?> listarClientes(Long id);

}
