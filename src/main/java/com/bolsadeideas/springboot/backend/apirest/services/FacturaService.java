package com.bolsadeideas.springboot.backend.apirest.services;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.bolsadeideas.springboot.backend.apirest.dto.RegistroItemFacturaDTO;

public interface FacturaService {
	
	
	ResponseEntity<?> listarPorCliente(Long idCliente, Pageable pageable);
	
	
	ResponseEntity<?> agregarItem(Long idFactura, RegistroItemFacturaDTO datos);
	
	
	ResponseEntity<?> listarItems(Long idFactura, Pageable pageable);
	
	
	ResponseEntity<?> eliminarItem(Long idFactura, Long idItem);
	
	ResponseEntity<?> encontrarItem(Long idFactura, Long idItem);

}
