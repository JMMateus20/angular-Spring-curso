package com.bolsadeideas.springboot.backend.apirest.services;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.bolsadeideas.springboot.backend.apirest.dto.RegistroModuloDTO;
import com.bolsadeideas.springboot.backend.apirest.dto.RegistroOperacionDTO;

public interface ModuloService {
	
	
	ResponseEntity<?> insertar(RegistroModuloDTO datos);

	ResponseEntity<?> agregarOperacion(Long moduloId, RegistroOperacionDTO datos);
	
	ResponseEntity<?> eliminarOperacion(Long moduloId, Long operacionId);
	
	ResponseEntity<?> listarOperaciones(Long moduloId, Pageable pageable);
	
	ResponseEntity<?> listarOperaciones2(Long moduloId);
	ResponseEntity<?> actualizarOperacion(Long moduloId, Long operacionId, RegistroOperacionDTO datos);
	ResponseEntity<?> modificarModulo(Long moduloId, RegistroModuloDTO datos);

}
